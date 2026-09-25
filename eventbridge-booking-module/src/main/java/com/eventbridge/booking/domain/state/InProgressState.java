package com.eventbridge.booking.domain.state;

import com.eventbridge.booking.domain.entity.Booking;
import com.eventbridge.booking.domain.enums.BookingStatus;

import java.time.LocalDateTime;

public class InProgressState extends AbstractBookingState {

    @Override
    public BookingStatus getStatus() {
        return BookingStatus.IN_PROGRESS;
    }

    @Override
    public void complete(Booking booking) {
        booking.setStatus(BookingStatus.COMPLETED);
        booking.setCompletedAt(LocalDateTime.now());
    }

    // Cancellation during event day is not supported through the normal path
}
