package com.eventbridge.booking.domain.state;

import com.eventbridge.booking.domain.entity.Booking;
import com.eventbridge.booking.domain.enums.BookingStatus;

/**
 * State Pattern interface.
 * Each concrete state knows which transitions are legal and what side-effects to apply.
 */
public interface BookingState {

    BookingStatus getStatus();

    void placeHold(Booking booking, int holdHours);

    void confirm(Booking booking);

    void startProgress(Booking booking);

    void complete(Booking booking);

    void cancel(Booking booking);
}
