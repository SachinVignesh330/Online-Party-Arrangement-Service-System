package com.eventbridge.booking.domain.state;

import com.eventbridge.booking.domain.entity.Booking;
import com.eventbridge.booking.domain.enums.BookingStatus;

public class ConfirmedState extends AbstractBookingState {

    @Override
    public BookingStatus getStatus() {
        return BookingStatus.CONFIRMED;
    }

    @Override
    public void startProgress(Booking booking) {
        booking.setStatus(BookingStatus.IN_PROGRESS);
    }

    @Override
    public void cancel(Booking booking) {
        // Business rule: cancellation after confirmation is still allowed
        // (refund policy is handled outside the state machine)
        booking.setStatus(BookingStatus.CANCELLED);
    }
}
