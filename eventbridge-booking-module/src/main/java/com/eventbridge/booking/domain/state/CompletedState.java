package com.eventbridge.booking.domain.state;

import com.eventbridge.booking.domain.enums.BookingStatus;

/**
 * Terminal state – no further transitions allowed.
 */
public class CompletedState extends AbstractBookingState {

    @Override
    public BookingStatus getStatus() {
        return BookingStatus.COMPLETED;
    }
}
