package com.eventbridge.booking.repository;

import com.eventbridge.booking.domain.entity.Payment;
import com.eventbridge.booking.domain.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    List<Payment> findByBooking_BookingId(Integer bookingId);

    Optional<Payment> findByTransactionReference(String transactionReference);

    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p " +
           "WHERE p.booking.bookingId = :bookingId AND p.paymentStatus = :status")
    BigDecimal sumSuccessfulPayments(@Param("bookingId") Integer bookingId,
                                     @Param("status") PaymentStatus status);
}
