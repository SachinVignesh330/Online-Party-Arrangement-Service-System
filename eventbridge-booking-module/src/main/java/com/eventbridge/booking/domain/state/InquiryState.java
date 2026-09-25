package com.eventbridge.booking.domain.state;

import com.eventbridge.booking.domain.entity.Booking;
import com.eventbridge.booking.domain.enums.BookingStatus;

import java.time.LocalDateTime;

public class InquiryState extends AbstractBookingState {

    @Override
    public BookingStatus getStatus() {
        return BookingStatus.INQUIRY;
    }

    @Override
    public void placeHold(Booking booking, int holdHours) {
        booking.setStatus(BookingStatus.PROVISIONAL_HOLD);
        booking.setHoldUntil(LocalDateTime.now().plusHours(holdHours));
    }

    @Override
    public void cancel(Booking booking) {
        booking.setStatus(BookingStatus.CANCELLED);
    }
}
