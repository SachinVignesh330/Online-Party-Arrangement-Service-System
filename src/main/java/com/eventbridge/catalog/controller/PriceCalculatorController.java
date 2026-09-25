package com.eventbridge.catalog.controller;

import com.eventbridge.catalog.dto.PriceQuoteResponse;
import com.eventbridge.catalog.service.PriceCalculatorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * REST endpoint for the live price calculator.
 * Base URL: /api/packages/{packageId}/price
 *
 * Called by the React frontend whenever the customer changes
 * guest count, catering option, or add-on selections.
 */
@RestController
@RequestMapping("/api/packages")
public class PriceCalculatorController {

    private final PriceCalculatorService priceCalculatorService;

    public PriceCalculatorController(PriceCalculatorService priceCalculatorService) {
        this.priceCalculatorService = priceCalculatorService;
    }

    /**
     * GET /api/packages/{packageId}/price
     *
     * Example request:
     * GET /api/packages/2/price?guestCount=80&cateringPerHead=500&addOnIds=1,3&isLoyaltyMember=false
     */
    @GetMapping("/{packageId}/price")
    public ResponseEntity<PriceQuoteResponse> getPrice(
            @PathVariable Integer packageId,
            @RequestParam int guestCount,
            @RequestParam BigDecimal cateringPerHead,
            @RequestParam(required = false, defaultValue = "") List<Integer> addOnIds,
            @RequestParam(defaultValue = "false") boolean isLoyaltyMember) {

        PriceQuoteResponse quote = priceCalculatorService.calculateQuote(
                packageId, guestCount, cateringPerHead, addOnIds, isLoyaltyMember);

        return ResponseEntity.ok(quote);
    }
}
