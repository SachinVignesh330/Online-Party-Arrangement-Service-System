package com.eventbridge.booking.repository;

import com.eventbridge.booking.domain.entity.Booking;
import com.eventbridge.booking.domain.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    List<Booking> findByCustomerId(Integer customerId);

    List<Booking> findByStatus(BookingStatus status);

    @Query("SELECT b FROM Booking b WHERE b.status = :status AND b.holdUntil < :now")
    List<Booking> findExpiredHolds(@Param("status") BookingStatus status,
                                   @Param("now") LocalDateTime now);

    boolean existsByCustomerIdAndEventDateAndStatusNot(
            Integer customerId,
            java.time.LocalDate eventDate,
            BookingStatus status);
}
