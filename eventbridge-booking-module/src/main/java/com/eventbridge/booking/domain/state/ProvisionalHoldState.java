package com.eventbridge.booking.domain.state;

import com.eventbridge.booking.domain.entity.Booking;
import com.eventbridge.booking.domain.enums.BookingStatus;
import com.eventbridge.booking.exception.HoldExpiredException;

import java.time.LocalDateTime;

public class ProvisionalHoldState extends AbstractBookingState {

    @Override
    public BookingStatus getStatus() {
        return BookingStatus.PROVISIONAL_HOLD;
    }

    @Override
    public void confirm(Booking booking) {
        if (booking.isHoldExpired()) {
            throw new HoldExpiredException(booking.getBookingId());
        }
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setConfirmedAt(LocalDateTime.now());
        booking.setHoldUntil(null); // hold is consumed
    }

    @Override
    public void cancel(Booking booking) {
        booking.setStatus(BookingStatus.CANCELLED);
        booking.setHoldUntil(null);
    }
}
