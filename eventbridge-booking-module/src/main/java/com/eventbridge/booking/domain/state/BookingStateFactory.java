package com.eventbridge.booking.domain.state;

import com.eventbridge.booking.domain.enums.BookingStatus;

/**
 * Simple factory that returns the correct state object for a given status.
 * Keeps the service layer free of switch statements (Open/Closed).
 */
public final class BookingStateFactory {

    private BookingStateFactory() {
    }

    public static BookingState from(BookingStatus status) {
        return switch (status) {
            case INQUIRY -> new InquiryState();
            case PROVISIONAL_HOLD -> new ProvisionalHoldState();
            case CONFIRMED -> new ConfirmedState();
            case IN_PROGRESS -> new InProgressState();
            case COMPLETED -> new CompletedState();
            case CANCELLED -> new CancelledState();
        };
    }
}
