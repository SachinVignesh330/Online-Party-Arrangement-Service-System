# EventBridge – Booking & Payment Module

## 1. About the Module

The Booking & Payment Module is part of the EventBridge Party Concierge system.

It is developed using **Spring Boot 3.2 and Java 17**.

This module is responsible for managing event bookings and processing payments. It allows customers to create bookings, reserve a date temporarily, make payments, and track the status of their bookings.

### Main Features

* Create and manage event bookings.
* Manage booking statuses from inquiry to completion or cancellation.
* Place a temporary booking hold for 48 hours.
* Process deposits and full payments.
* Automatically confirm a booking when payment is successful.
* Calculate booking prices, including add-ons.
* Handle invalid booking operations and payment errors.

---

## 2. Design Patterns Used

Design patterns are common ways of organizing code to make it easier to understand, maintain, and extend.

| Design Pattern   | How It Is Used                                                                                      |
| ---------------- | --------------------------------------------------------------------------------------------------- |
| State Pattern    | Manages the booking lifecycle. Each booking state defines which actions are allowed.                |
| Strategy Pattern | Allows different payment gateways to be used without changing the payment service.                  |
| Factory Pattern  | Creates the correct booking state based on the current booking status.                              |
| Template Method  | Provides common rules for booking states and allows individual states to define their own behavior. |

### Why These Patterns Are Useful

* Booking rules are kept separate from the main service logic.
* New booking states or payment gateways can be added more easily.
* The code is easier to maintain and test.

---

## 3. Object-Oriented Programming Concepts

The module uses the main concepts of Object-Oriented Programming (OOP).

| OOP Concept   | Simple Explanation                                                                       | Example in the Module                                                    |
| ------------- | ---------------------------------------------------------------------------------------- | ------------------------------------------------------------------------ |
| Encapsulation | Keeps data and related operations together while controlling access to the data.         | `Booking` and `Payment` entities use private fields and getters/setters. |
| Abstraction   | Hides implementation details and shows only the required operations.                     | `BookingState` and `PaymentGateway` interfaces.                          |
| Inheritance   | Allows one class to reuse the features of another class.                                 | Booking state classes extend `AbstractBookingState`.                     |
| Polymorphism  | Allows the same method to behave differently depending on the object.                    | `state.confirm()` behaves according to the current booking state.        |
| Composition   | Represents a relationship where one object contains or is associated with other objects. | A booking can have add-ons and payments.                                 |

### SOLID Principles

The module also follows several SOLID principles:

* **Single Responsibility Principle:** Each class has a specific job. For example, `BookingService` manages booking operations, while `PaymentService` manages payments.
* **Open/Closed Principle:** New booking states and payment gateways can be added without unnecessarily changing existing service code.
* **Dependency Inversion Principle:** `PaymentService` depends on the `PaymentGateway` interface rather than directly depending on `MockPaymentGateway`.

The module also follows DRY (avoid repeating code), KISS (keep the design simple), and YAGNI (avoid adding features before they are needed).

---

## 4. Booking Lifecycle

A booking moves through different stages during its lifetime.

```text
Inquiry
   |
   | Place a temporary hold
   v
Provisional Hold (48 hours)
   |
   | Successful payment / confirmation
   v
Confirmed
   |
   | Event starts
   v
In Progress
   |
   | Event completed
   v
Completed
```

A booking can also be cancelled before reaching a terminal state.

### Booking States

| State            | Meaning                                                            |
| ---------------- | ------------------------------------------------------------------ |
| Inquiry          | The customer has created a booking request.                        |
| Provisional Hold | The booking is temporarily held for 48 hours.                      |
| Confirmed        | The booking has been confirmed, normally after successful payment. |
| In Progress      | The event is currently taking place or being carried out.          |
| Completed        | The event has been completed.                                      |
| Cancelled        | The booking has been cancelled.                                    |

If a customer does not complete the required payment within the hold period, the scheduled job can release the expired hold.

Invalid status changes are rejected using `InvalidBookingTransitionException`.

---

## 5. How the Booking State Pattern Works

The State Pattern controls which actions are allowed for a booking.

For example, a booking in the Inquiry state can be placed on hold, but a completed booking cannot be placed on hold again.

The following classes are involved:

| Class                  | Responsibility                                                 |
| ---------------------- | -------------------------------------------------------------- |
| `BookingState`         | Defines the operations available for changing booking states.  |
| `AbstractBookingState` | Provides default behavior for operations that are not allowed. |
| `InquiryState`         | Handles actions allowed during inquiry.                        |
| `ProvisionalHoldState` | Handles actions allowed while the booking is on hold.          |
| `ConfirmedState`       | Handles actions allowed after confirmation.                    |
| `InProgressState`      | Handles actions allowed while the event is in progress.        |
| `CompletedState`       | Represents a completed booking.                                |
| `CancelledState`       | Represents a cancelled booking.                                |
| `BookingStateFactory`  | Returns the correct state object based on the booking status.  |

This keeps booking rules in the state classes instead of placing all the conditions inside `BookingService`.

---

## 6. How Payment Processing Works

The module uses the Strategy Pattern to support different payment gateways.

A payment gateway is the service that processes a customer's payment.

### Payment Components

| Class                    | Responsibility                                                       |
| ------------------------ | -------------------------------------------------------------------- |
| `PaymentGateway`         | Defines the common operations that a payment gateway must provide.   |
| `MockPaymentGateway`     | Simulates payment processing during development.                     |
| `PaymentGatewayResolver` | Selects the appropriate payment gateway based on the payment method. |
| `PaymentService`         | Manages the payment process and updates payment and booking records. |

### Payment Flow

```text
Customer submits payment
          |
          v
    PaymentService
          |
          v
 PaymentGatewayResolver
          |
          v
   PaymentGateway
          |
          v
 MockPaymentGateway
          |
          v
 Payment result returned
          |
          v
 Payment record saved
          |
          v
Successful payment?
       /       \
     Yes        No
      |          |
Confirm booking  Handle payment failure
```

The current implementation uses a mock payment gateway for local development.

A real payment gateway, such as PayHere or Stripe, can be integrated later by implementing the `PaymentGateway` interface and configuring the appropriate gateway.

A real integration must use the provider's secure payment flow and must not store raw card numbers or CVV values in the EventBridge database.

---

## 7. REST API Endpoints

The module provides REST APIs that the frontend can use to communicate with the backend.

### Booking APIs

Base URL: `/api/v1/bookings`

| HTTP Method | Endpoint                 | Description                                |
| ----------- | ------------------------ | ------------------------------------------ |
| POST        | `/`                      | Create a new booking with Inquiry status.  |
| GET         | `/{id}`                  | Get booking details and total amount paid. |
| GET         | `/customer/{customerId}` | Get bookings belonging to a customer.      |
| POST        | `/{id}/hold`             | Place a temporary hold on a booking.       |
| POST        | `/{id}/confirm`          | Confirm a booking that is on hold.         |
| POST        | `/{id}/start`            | Start a confirmed booking.                 |
| POST        | `/{id}/complete`         | Mark an in-progress booking as completed.  |
| POST        | `/{id}/cancel`           | Cancel a booking.                          |

### Payment APIs

Base URL: `/api/v1/payments`

| HTTP Method | Endpoint               | Description                             |
| ----------- | ---------------------- | --------------------------------------- |
| POST        | `/`                    | Process a payment for a booking.        |
| GET         | `/{id}`                | Get payment details.                    |
| GET         | `/booking/{bookingId}` | Get payments associated with a booking. |

A successful deposit or full payment can automatically confirm a booking that is on provisional hold.

---

## 8. Example Booking and Payment Flow

The following example shows how the frontend can use the APIs.

### Step 1: Create a booking

```http
POST /api/v1/bookings
Content-Type: application/json
```

Request:

```json
{
  "customerId": 1,
  "packageId": 3,
  "eventDate": "2026-10-15",
  "location": "Garden Lawn, Colombo",
  "guestCount": 150,
  "addOns": [
    {
      "addonId": 2,
      "quantity": 1
    }
  ]
}
```

The system creates a new booking with Inquiry status.

### Step 2: Place the booking on hold

```http
POST /api/v1/bookings/1/hold
```

The booking moves to Provisional Hold for 48 hours.

### Step 3: Make a deposit payment

```http
POST /api/v1/payments
Content-Type: application/json
```

Request:

```json
{
  "bookingId": 1,
  "amount": 30000.00,
  "paymentMethod": "CARD",
  "paymentType": "DEPOSIT"
}
```

If the payment is successful, the system saves the payment and automatically confirms the booking.

---

## 9. Integration with Other Modules

EventBridge contains several modules that work together. Some features will be connected when the other modules are integrated.

| Module                  | Integration                                                                                  |
| ----------------------- | -------------------------------------------------------------------------------------------- |
| Catalog & Package       | Provides package prices and add-on prices used to calculate the booking total.               |
| Inventory & Reservation | Will handle reserving and releasing inventory when bookings are placed on hold or cancelled. |
| Vendor & Bidding        | Will support vendor assignment after a booking is confirmed.                                 |
| Payment Gateway         | A real payment provider can replace the mock gateway through the `PaymentGateway` interface. |

These integration points allow the Booking & Payment Module to work independently during development and connect with the other EventBridge modules later.

---

## 10. Technology Stack

| Technology      | Purpose                                      |
| --------------- | -------------------------------------------- |
| Java 17         | Programming language.                        |
| Spring Boot 3.2 | Backend application framework.               |
| Spring Web      | Creates REST APIs.                           |
| Spring Data JPA | Communicates with the database.              |
| Hibernate       | Maps Java entities to database tables.       |
| PostgreSQL      | Stores booking and payment information.      |
| Maven           | Manages dependencies and builds the project. |

---

## 11. Running the Project

1. Install JDK 17 or a compatible configured JDK.
2. Install and start PostgreSQL.
3. Configure the database connection in `src/main/resources/application.yml`.
4. Open the project containing `pom.xml` in IntelliJ IDEA.
5. Run `BookingPaymentApplication.java`.

The application starts as a Spring Boot web application and exposes the REST APIs on the configured server port.

For local development, the mock payment gateway is used instead of a real payment provider.
