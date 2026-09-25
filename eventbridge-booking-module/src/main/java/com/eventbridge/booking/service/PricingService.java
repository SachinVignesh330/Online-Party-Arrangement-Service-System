package com.eventbridge.booking.service;

import com.eventbridge.booking.domain.entity.Booking;
import com.eventbridge.booking.domain.entity.BookingAddOn;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Single Responsibility: calculate monetary totals for a booking.
 * In a full system this would also look up package base_price and add-on catalog prices.
 * Here we use the unit prices already stored on BookingAddOn (set at booking time).
 */
@Service
public class PricingService {

    /**
     * Sum of all add-on line totals.
     * Package base price should be injected by the Catalog module in a full implementation.
     */
    public BigDecimal calculateAddOnTotal(Booking booking) {
        return booking.getAddOns().stream()
                .map(BookingAddOn::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Placeholder – real implementation would call Catalog module for package.base_price.
     */
    public BigDecimal estimateTotal(Booking booking, BigDecimal packageBasePrice) {
        BigDecimal base = packageBasePrice != null ? packageBasePrice : BigDecimal.ZERO;
        return base.add(calculateAddOnTotal(booking));
    }
}
