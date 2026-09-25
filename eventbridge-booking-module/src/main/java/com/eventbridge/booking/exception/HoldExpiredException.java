package com.eventbridge.booking.exception;

public class HoldExpiredException extends RuntimeException {

    public HoldExpiredException(Integer bookingId) {
        super("Provisional hold has expired for booking id: " + bookingId);
    }
}
