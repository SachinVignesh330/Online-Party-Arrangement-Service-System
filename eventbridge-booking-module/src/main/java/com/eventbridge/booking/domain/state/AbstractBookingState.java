package com.eventbridge.booking.domain.state;

import com.eventbridge.booking.domain.entity.Booking;
import com.eventbridge.booking.exception.InvalidBookingTransitionException;

/**
 * Template-method base that provides default "illegal transition" behaviour.
 * Concrete states override only the transitions they support (Open/Closed Principle).
 */
public abstract class AbstractBookingState implements BookingState {

    @Override
    public void placeHold(Booking booking, int holdHours) {
        throw new InvalidBookingTransitionException(getStatus(), "placeHold");
    }

    @Override
    public void confirm(Booking booking) {
        throw new InvalidBookingTransitionException(getStatus(), "confirm");
    }

    @Override
    public void startProgress(Booking booking) {
        throw new InvalidBookingTransitionException(getStatus(), "startProgress");
    }

    @Override
    public void complete(Booking booking) {
        throw new InvalidBookingTransitionException(getStatus(), "complete");
    }

    @Override
    public void cancel(Booking booking) {
        throw new InvalidBookingTransitionException(getStatus(), "cancel");
    }
}
