package com.eventbridge.booking.domain.enums;

/**
 * Distinguishes deposit vs full settlement vs residual balance payments.
 */
public enum PaymentType {
    DEPOSIT,
    FULL,
    BALANCE,
    REFUND
}
