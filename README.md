# Online-Party-Management-System
IT 4005 Final Group Assignment

# EventBridge - Catalog & Package Module

**Spring Boot 4.1.1 | PostgreSQL 18 | Java 17 | Maven**

EventBridge is a full-service event coordination platform. The **Catalog & Package Module** is one of the core business logic modules in the 4-layer Spring Boot architecture. It owns all business rules for the service packages and add-on items that customers browse, customise, and book.

---

## What This Module Does
* Manages standard package tiers: **Basic, Standard, Premium**, and a **Custom** quote-based tier.
* Manages the add-on catalogue (extra hours, lighting rigs, live food stations, etc.).
* Enforces package lifecycle (**Active $\rightarrow$ Inactive $\rightarrow$ Archived**) through encapsulated domain methods.
* Powers the live price calculator, showing customers a full cost breakdown before confirming.
* Applies the correct pricing strategy: **Standard** or **Loyalty 10% discount**, selected at runtime.

*Note: This module does not own bookings, payments, inventory, or vendor assignments. It exposes data and pricing that the Booking and Payment Module consumes.*

---

## Package Structure
```text
catalog/
├── enums/       # PackageTier, PackageStatus, AddOnStatus
├── entity/      # Package, AddOn @Entity JPA classes
├── dto/         # PackageRequest, PackageResponse, AddOnRequest, AddOnResponse, PriceQuoteResponse
├── strategy/    # PricingStrategy interface, Standard & Loyalty Pricing Strategies
├── repository/  # PackageRepository, AddOnRepository - Spring Data JPA interfaces
├── service/     # PackageService, AddOnService, PriceCalculatorService
├── mapper/      # PackageMapper, AddOnMapper (entity-to-DTO conversion)
├── controller/  # PackageController, AddOnController, PriceCalculatorController
└── exception/   # ResourceNotFoundException, DuplicatePackageException, GlobalExceptionHandler
