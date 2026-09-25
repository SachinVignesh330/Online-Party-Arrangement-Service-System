package com.eventbridge.catalog.strategy;

import com.eventbridge.catalog.entity.AddOn;
import com.eventbridge.catalog.entity.Package;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * Standard pricing: basePrice + (cateringPerHead × guestCount) + sum(add-on prices).
 *
 * This is the default strategy for first-time customers or customers
 * who have fewer than 2 completed bookings.
 *
 * DRY: add-on summation logic is extracted here once; no duplication
 *      in the loyalty or promotional strategies (they call super / delegate).
 */
@Component("standardPricing")
public class StandardPricingStrategy implements PricingStrategy {

    @Override
    public BigDecimal calculate(Package pkg,
                                int guestCount,
                                BigDecimal cateringPerHead,
                                List<AddOn> selectedAddOns) {

        BigDecimal base       = pkg.getBasePrice();
        BigDecimal catering   = cateringPerHead.multiply(BigDecimal.valueOf(guestCount));
        BigDecimal addOnTotal = sumAddOns(selectedAddOns);

        return base.add(catering).add(addOnTotal);
    }

    @Override
    public String getLabel() {
        return "Standard pricing";
    }

    /**
     * Shared helper — extracted so LoyaltyPricingStrategy can reuse
     * the summation without copy-pasting (DRY).
     */
    protected BigDecimal sumAddOns(List<AddOn> addOns) {
        return addOns.stream()
                .map(AddOn::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
