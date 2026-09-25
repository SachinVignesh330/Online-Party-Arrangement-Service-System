# EventBridge – Booking & Payment Module

Spring Boot 3.2 / Java 17 module that implements the **Booking lifecycle** and **Payment processing** for EventBridge Party Concierge.

---

## Design Decisions

| Principle / Pattern         | How it is applied                                                                                                                                                                                                     |
| --------------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **State Pattern**           | `BookingState` hierarchy + `BookingStateFactory` control all lifecycle transitions (Inquiry → Hold → Confirmed → In Progress → Completed / Cancelled). Illegal transitions throw `InvalidBookingTransitionException`. |
| **Strategy Pattern**        | `PaymentGateway` interface + `PaymentGatewayResolver`. Swap Mock ↔ real PCI gateway without touching service code.                                                                                                    |
| **Factory Pattern**         | `BookingStateFactory.from(status)` returns the correct state object and keeps the service layer free of switch statements.                                                                                            |
| **Template Method**         | `AbstractBookingState` provides default “illegal transition” behaviour; concrete states override only the transitions they allow.                                                                                     |
| **SOLID**                   | Single-responsibility services (`BookingService`, `PaymentService`, `PricingService`). Open/Closed via state & strategy. Dependency Inversion on `PaymentGateway`.                                                    |
| **DRY / KISS / YAGNI**      | Shared `AbstractBookingState`, minimal DTOs, no premature inventory/vendor locking inside this module (hooks left for other modules).                                                                                 |
| **Transactional integrity** | All state-changing methods are `@Transactional`. Successful DEPOSIT/FULL payment automatically confirms a provisional hold.                                                                                           |

---

## Object-Oriented Concepts Used

| OOP Concept        | Where it appears in this module                                                                                                                                 |
| ------------------ | --------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Encapsulation**  | Entity fields are private with getters/setters (`Booking`, `Payment`, `BookingAddOn`). Business rules for transitions live inside state classes, not scattered in controllers. |
| **Abstraction**    | `BookingState` and `PaymentGateway` interfaces hide concrete implementations from `BookingService` and `PaymentService`.                                         |
| **Inheritance**    | `AbstractBookingState` is extended by `InquiryState`, `ProvisionalHoldState`, `ConfirmedState`, `InProgressState`, `CompletedState`, and `CancelledState`.      |
| **Polymorphism**   | `BookingService` calls `state.placeHold()`, `state.confirm()`, etc. without knowing the concrete state class. Behaviour changes at runtime based on current status. |
| **Composition**    | A `Booking` *has* a list of `BookingAddOn` and `Payment` objects (one-to-many relationships).                                                                    |
| **Single Responsibility** | Each class has one reason to change: services orchestrate, states own transition rules, gateways own payment processing, repositories own persistence.   |
| **Open/Closed Principle** | New booking states or payment gateways can be added by creating new classes; existing service code does not need modification.                              |
| **Dependency Inversion** | High-level modules (`PaymentService`) depend on the `PaymentGateway` abstraction, not on `MockPaymentGateway`.                                               |

### Class relationships (simplified)

```
BookingService
    └── uses BookingStateFactory
            └── returns BookingState (interface)
                    ├── InquiryState
                    ├── ProvisionalHoldState
                    ├── ConfirmedState
                    ├── InProgressState
                    ├── CompletedState
                    └── CancelledState
                    (all extend AbstractBookingState)

PaymentService
    └── uses PaymentGatewayResolver
            └── resolves PaymentGateway (interface)
                    └── MockPaymentGateway  (+ future real gateways)
```

---

## Design Patterns (detail)

### 1. State Pattern (Booking lifecycle)

- **Intent:** Allow a booking to alter its behaviour when its internal status changes.
- **Structure:**
    - `BookingState` – interface defining `placeHold`, `confirm`, `startProgress`, `complete`, `cancel`
    - `AbstractBookingState` – default illegal-transition implementations
    - Concrete states – each allows only legal next steps and updates timestamps/status
    - `BookingStateFactory` – maps `BookingStatus` enum → concrete state instance
- **Benefit:** Lifecycle rules stay in one place; adding a new status does not bloat `BookingService`.

### 2. Strategy Pattern (Payment processing)

- **Intent:** Define a family of payment algorithms and make them interchangeable.
- **Structure:**
    - `PaymentGateway` – strategy interface (`process`, `supports`)
    - `MockPaymentGateway` – development strategy (always succeeds for valid amounts)
    - `PaymentGatewayResolver` – selects a strategy by `PaymentMethod` at runtime
- **Benefit:** A real PCI-compliant gateway (e.g. Stripe, PayHere) can be plugged in later without changing `PaymentService` or the REST API.

### 3. Factory Pattern

- `BookingStateFactory.from(BookingStatus)` centralises creation of state objects and avoids switch/if chains in the service layer.

### 4. Template Method (light)

- `AbstractBookingState` defines the skeleton of transition handling; subclasses override only supported operations.

---

## Booking Lifecycle (from Requirements §8)

```
Inquiry
   │ placeHold()
   ▼
Provisional Hold  (48 h default – auto-released by scheduled job)
   │ confirm()  ← normally triggered by successful payment
   ▼
Confirmed
   │ startProgress()
   ▼
In Progress
   │ complete()
   ▼
Completed

Any non-terminal state → cancel()
```

---

## REST API

### Bookings  `/api/v1/bookings`

| Method | Path                     | Description                       |
| ------ | ------------------------ | --------------------------------- |
| POST   | `/`                      | Create booking (status = INQUIRY) |
| GET    | `/{id}`                  | Get booking details + total paid  |
| GET    | `/customer/{customerId}` | List customer bookings            |
| POST   | `/{id}/hold`             | Inquiry → Provisional Hold        |
| POST   | `/{id}/confirm`          | Hold → Confirmed                  |
| POST   | `/{id}/start`            | Confirmed → In Progress           |
| POST   | `/{id}/complete`         | In Progress → Completed           |
| POST   | `/{id}/cancel`           | Cancel (releases hold)            |

### Payments  `/api/v1/payments`

| Method | Path                   | Description                                     |
| ------ | ---------------------- | ----------------------------------------------- |
| POST   | `/`                    | Process payment (auto-confirms hold on success) |
| GET    | `/{id}`                | Get payment by id                               |
| GET    | `/booking/{bookingId}` | List payments for a booking                     |

---

## Example flow

```http
### 1. Create booking
POST /api/v1/bookings
{
  "customerId": 1,
  "packageId": 3,
  "eventDate": "2026-10-15",
  "location": "Garden Lawn, Colombo",
  "guestCount": 150,
  "addOns": [
    { "addonId": 2, "quantity": 1 }
  ]
}

### 2. Place 48-hour provisional hold
POST /api/v1/bookings/1/hold

### 3. Pay deposit → automatically confirms booking
POST /api/v1/payments
{
  "bookingId": 1,
  "amount": 30000.00,
  "paymentMethod": "CARD",
  "paymentType": "DEPOSIT"
}
```

---

## Integration points (left intentionally open)

- **Catalog module** – supply `package.base_price` and live add-on unit prices into `PricingService` / `BookingAddOn.unitPrice`.
- **Inventory & Reservation module** – lock/release inventory inside `placeHold()` / `cancel()`.
- **Vendor & Bidding module** – assign vendors when booking is confirmed.
- Real **PCI-compliant gateway** – implement `PaymentGateway` and register the bean; `MockPaymentGateway` is the default for local development.

---


