package com.eventbridge.catalog.strategy;

import com.eventbridge.catalog.entity.AddOn;
import com.eventbridge.catalog.entity.Package;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Loyalty pricing: applies a configurable % discount for repeat customers
 * (customers who have 2+ completed bookings, per the business spec).
 *
 * OOP – Inheritance: extends StandardPricingStrategy to reuse add-on
 *       summation (DRY) and only overrides the final calculation step.
 *
 * SOLID – OCP: adding this class required zero changes to StandardPricingStrategy.
 *
 * KISS: the discount is a simple multiplier — no complex rule tree.
 * YAGNI: only one loyalty tier is defined today; more can be added via
 *         new strategy classes without touching this one.
 */
@Component("loyaltyPricing")
public class LoyaltyPricingStrategy extends StandardPricingStrategy {

    private static final BigDecimal DISCOUNT_RATE = new BigDecimal("0.10"); // 10%

    @Override
    public BigDecimal calculate(Package pkg,
                                int guestCount,
                                BigDecimal cateringPerHead,
                                List<AddOn> selectedAddOns) {

        BigDecimal standardTotal = super.calculate(pkg, guestCount, cateringPerHead, selectedAddOns);
        BigDecimal discount      = standardTotal.multiply(DISCOUNT_RATE)
                .setScale(2, RoundingMode.HALF_UP);
        return standardTotal.subtract(discount);
    }

    @Override
    public String getLabel() {
        return "Loyalty pricing (10% discount applied)";
    }
}
