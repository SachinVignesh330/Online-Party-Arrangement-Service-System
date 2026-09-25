package com.eventbridge.catalog.strategy;

import com.eventbridge.catalog.entity.AddOn;
import com.eventbridge.catalog.entity.Package;

import java.math.BigDecimal;
import java.util.List;

/**
 * Strategy Pattern — pricing interface.
 *
 * WHY A STRATEGY HERE?
 * EventBridge has multiple pricing rules that can vary independently:
 *   • Standard pricing  — base price + catering-per-head + add-on costs.
 *   • Loyalty pricing   — repeat customers (2+ bookings) get a discount.
 *   • Promotional pricing — time-limited promotional discounts.
 *
 * Without this interface, all pricing branches would live inside one
 * service method (violating OCP).  Adding a new pricing tier would
 * require modifying existing tested code.
 *
 * With the Strategy pattern:
 *   • Each rule is an isolated, testable class.
 *   • PriceCalculatorService receives the correct strategy at runtime
 *     (injected by the Booking module based on customer loyalty status).
 *   • OCP: open for extension (new strategy), closed for modification.
 *
 * SOLID – OCP, DIP: the service depends on this abstraction, not concretes.
 */
public interface PricingStrategy {

    /**
     * Calculates the total quoted price for a package booking.
     *
     * @param pkg          the chosen package
     * @param guestCount   number of guests (drives per-head catering cost)
     * @param cateringPerHead cost per guest for the chosen catering option
     * @param selectedAddOns add-ons the customer has selected
     * @return total price (never null, never negative)
     */
    BigDecimal calculate(Package pkg,
                         int guestCount,
                         BigDecimal cateringPerHead,
                         List<AddOn> selectedAddOns);

    /**
     * Human-readable label for UI display (e.g. "Standard pricing",
     * "Loyalty 10% discount applied").
     */
    String getLabel();
}
