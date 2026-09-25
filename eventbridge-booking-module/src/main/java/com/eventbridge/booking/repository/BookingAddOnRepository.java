package com.eventbridge.booking.repository;

import com.eventbridge.booking.domain.entity.BookingAddOn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingAddOnRepository extends JpaRepository<BookingAddOn, Integer> {

    List<BookingAddOn> findByBooking_BookingId(Integer bookingId);
}
