package com.eventbridge.booking.domain.state;

import com.eventbridge.booking.domain.enums.BookingStatus;

/**
 * Terminal state – no further transitions allowed.
 */
public class CancelledState extends AbstractBookingState {

    @Override
    public BookingStatus getStatus() {
        return BookingStatus.CANCELLED;
    }
}
