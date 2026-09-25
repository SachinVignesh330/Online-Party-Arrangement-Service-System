package com.eventbridge.booking.exception;

public class BookingNotFoundException extends RuntimeException {

    public BookingNotFoundException(Integer bookingId) {
        super("Booking not found with id: " + bookingId);
    }
}
