package com.eventbridge.catalog.service;

import org.springframework.beans.factory.annotation.Qualifier;
import com.eventbridge.catalog.dto.PriceQuoteResponse;
import com.eventbridge.catalog.entity.AddOn;
import com.eventbridge.catalog.entity.Package;
import com.eventbridge.catalog.exception.ResourceNotFoundException;
import com.eventbridge.catalog.repository.AddOnRepository;
import com.eventbridge.catalog.repository.PackageRepository;
import com.eventbridge.catalog.strategy.LoyaltyPricingStrategy;
import com.eventbridge.catalog.strategy.PricingStrategy;
import com.eventbridge.catalog.strategy.StandardPricingStrategy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Powers the live price calculator the customer sees before booking.
 *
 * Design Pattern — Strategy:
 *   Selects the correct PricingStrategy at runtime based on whether
 *   the customer is a loyalty member (2+ completed bookings).
 *   The controller passes isLoyaltyMember from the Booking module's
 *   customer check — this service doesn't need to know how that's determined.
 *
 * SOLID – OCP: adding a new pricing tier (e.g. PromoStrategy) requires
 *              zero changes here — just inject the new strategy.
 *         DIP: depends on PricingStrategy interface, not concrete classes.
 *
 * KISS: the strategy selection is a single if/else — no complex rule engine.
 * YAGNI: only two strategies exist today; more added only when needed.
 */
@Service
@Transactional(readOnly = true)
public class PriceCalculatorService {

    private final PackageRepository        packageRepository;
    private final AddOnRepository          addOnRepository;
    private final StandardPricingStrategy  standardPricingStrategy;
    private final LoyaltyPricingStrategy   loyaltyPricingStrategy;

    public PriceCalculatorService(PackageRepository packageRepository,
                                  AddOnRepository addOnRepository,
                                  @Qualifier("standardPricing")
                                  StandardPricingStrategy standardPricingStrategy,
                                  @Qualifier("loyaltyPricing")
                                  LoyaltyPricingStrategy loyaltyPricingStrategy) {
        this.packageRepository       = packageRepository;
        this.addOnRepository         = addOnRepository;
        this.standardPricingStrategy = standardPricingStrategy;
        this.loyaltyPricingStrategy  = loyaltyPricingStrategy;
    }

    /**
     * Calculates and returns a full price breakdown.
     *
     * @param packageId       the package being priced
     * @param guestCount      number of guests
     * @param cateringPerHead cost per guest for the selected catering option
     * @param addOnIds        IDs of add-ons the customer has selected
     * @param isLoyaltyMember true if the customer has 2+ completed bookings
     */
    public PriceQuoteResponse calculateQuote(Integer packageId,
                                             int guestCount,
                                             BigDecimal cateringPerHead,
                                             List<Integer> addOnIds,
                                             boolean isLoyaltyMember) {

        // Load package
        Package pkg = packageRepository.findById(packageId)
                .orElseThrow(() -> new ResourceNotFoundException("Package", packageId));

        // Load selected add-ons
        List<AddOn> selectedAddOns = addOnRepository.findAllById(addOnIds);

        // Strategy selection — runtime decision (Strategy Pattern)
        PricingStrategy strategy = isLoyaltyMember
                ? loyaltyPricingStrategy
                : standardPricingStrategy;

        BigDecimal grandTotal = strategy.calculate(pkg, guestCount, cateringPerHead, selectedAddOns);

        // Build detailed breakdown for the UI
        BigDecimal cateringTotal = cateringPerHead.multiply(BigDecimal.valueOf(guestCount));
        BigDecimal addOnTotal    = selectedAddOns.stream()
                .map(AddOn::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<String> addOnNames = selectedAddOns.stream()
                .map(AddOn::getName)
                .collect(Collectors.toList());

        PriceQuoteResponse response = new PriceQuoteResponse();
        response.setPackageId(pkg.getPackageId());
        response.setPackageName(pkg.getPackageName());
        response.setPricingLabel(strategy.getLabel());
        response.setBasePrice(pkg.getBasePrice());
        response.setCateringTotal(cateringTotal);
        response.setAddOnTotal(addOnTotal);
        response.setGrandTotal(grandTotal);
        response.setAppliedAddOns(addOnNames);

        return response;
    }
}
