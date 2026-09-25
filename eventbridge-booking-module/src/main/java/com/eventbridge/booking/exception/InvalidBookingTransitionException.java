package com.eventbridge.booking.exception;

import com.eventbridge.booking.domain.enums.BookingStatus;

public class InvalidBookingTransitionException extends RuntimeException {

    public InvalidBookingTransitionException(BookingStatus current, String action) {
        super(String.format("Cannot perform '%s' while booking is in state %s", action, current));
    }
}
