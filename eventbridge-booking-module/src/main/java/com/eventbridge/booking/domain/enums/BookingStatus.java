package com.eventbridge.booking.domain.enums;

/**
 * Booking lifecycle states as defined in the business requirements.
 * Inquiry → Provisional Hold → Confirmed → In Progress → Completed
 * Any active state may transition to Cancelled.
 */
public enum BookingStatus {
    INQUIRY,
    PROVISIONAL_HOLD,
    CONFIRMED,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED
}
