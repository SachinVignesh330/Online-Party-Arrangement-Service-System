# Event-Day Operations Module

Part of the EventBridge Online Party Arrangement Service System. This module handles event coordinators and the auto-generated day-of checklist for confirmed bookings.

## What it does

- **Coordinator management** — create/view coordinators who can be assigned to events.
- **Checklist auto-generation** — given a booking ID and package tier (Basic/Standard/Premium), generates the correct set of day-of tasks per EventBridge's business rules (e.g. Premium bookings get a dedicated full-day coordinator check-in; Basic bookings don't get caterer/DJ tasks).
- **Checklist tracking** — mark individual tasks complete as the coordinator works through the event.
- Checklist generation is protected by a real foreign key constraint — it requires a booking ID that already exists in the `booking` table. Confirmed by testing: attempting to generate a checklist against a non-existent booking correctly fails with a database integrity error rather than silently creating bad data.
## Tech stack

- Java 17
- Spring Boot 3.3 (Spring Web, Spring Data JPA)
- PostgreSQL

## How to run it

1. Make sure Postgres is running with the `eventbridge` database and the shared team schema loaded.
2. Update the credentials in `src/main/resources/application.properties` to match your local Postgres setup.
3. Run the app: `mvn spring-boot:run`, or open the project in IntelliJ and run `EventdayApplication`.
4. App starts on `http://localhost:8080`.

## Endpoints

| Method | Path | Description |
|---|---|---|
| GET | `/api/coordinators` | List all coordinators |
| POST | `/api/coordinators` | Create a coordinator |
| GET | `/api/coordinators/{id}` | Get one coordinator |
| POST | `/api/bookings/{bookingId}/checklist/generate?tier=Premium` | Generate checklist for a booking |
| GET | `/api/bookings/{bookingId}/checklist` | View a booking's checklist |
| PATCH | `/api/bookings/{bookingId}/checklist/{itemId}/complete` | Mark a checklist item complete |

Tested manually with curl/Postman against a local Postgres instance — all endpoints return the expected JSON for create, list, and update operations.

## OOP concepts used

- **Encapsulation** — `Coordinator` and `ChecklistItem` keep their fields `private` and expose them only through getters/setters, so no other class can put them into an invalid state directly.
- **Abstraction** — `CoordinatorRepository` and `ChecklistItemRepository` are interfaces (extending `JpaRepository`). Controllers and services depend on *what* the repository can do (`save`, `findById`, ...), not on how it talks to Postgres underneath.
- **Single Responsibility Principle** — each class has one job: `Controller` classes only handle HTTP request/response, `Service` classes only hold business logic (e.g. which checklist tasks a tier gets), `Repository` interfaces only handle data access, `Entity` classes only represent a database row.

## Design patterns used

- **Layered (N-tier) Architecture** — Controller → Service → Repository → Entity, matching the rest of the EventBridge system's 4-layer design (Presentation / Business Logic / Data Access / Database). Each layer only calls the one directly below it.
- **Repository Pattern** — `CoordinatorRepository` / `ChecklistItemRepository` abstract away persistence logic behind a simple interface, so the service layer never writes raw SQL or JDBC code.
- **Dependency Injection (Inversion of Control)** — controllers and services receive their dependencies (e.g. `ChecklistService` receives `ChecklistItemRepository`) through the constructor rather than creating them, and Spring's IoC container wires them together at startup. This is what makes the classes independently testable.
- **Singleton** — Spring manages `@Service` and `@RestController` beans as singletons by default, so there's exactly one shared `ChecklistService` instance handling all requests.

## Known limitations / not yet implemented

- Real-time issue-flagging (late vendor, missing equipment) — needs a new `event_issue` table not present in the original schema.
- Reporting endpoints (revenue by tier, inventory utilization, etc.).
- Not yet wired to the real `Booking` entity — checklist currently stores a plain `bookingId` integer until the Booking module is merged in.
- For local testing without the real Booking module built yet, a manual test row was inserted directly into `customer`, `package`, and `booking` via SQL to satisfy the foreign key requirement. Once a teammate's Booking module is merged, real booking data will exist naturally.