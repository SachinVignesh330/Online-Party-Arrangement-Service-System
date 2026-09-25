package com.eventbridge.booking.service;

import com.eventbridge.booking.domain.entity.Booking;
import com.eventbridge.booking.domain.entity.Payment;
import com.eventbridge.booking.domain.enums.BookingStatus;
import com.eventbridge.booking.domain.enums.PaymentStatus;
import com.eventbridge.booking.domain.enums.PaymentType;
import com.eventbridge.booking.dto.request.PaymentRequest;
import com.eventbridge.booking.dto.response.PaymentResponse;
import com.eventbridge.booking.exception.BookingNotFoundException;
import com.eventbridge.booking.exception.PaymentProcessingException;
import com.eventbridge.booking.repository.BookingRepository;
import com.eventbridge.booking.repository.PaymentRepository;
import com.eventbridge.booking.service.payment.PaymentGateway;
import com.eventbridge.booking.service.payment.PaymentGatewayResolver;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles payment creation, gateway interaction, and automatic booking confirmation
 * when a DEPOSIT or FULL payment succeeds while the booking is on provisional hold.
 */
@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final PaymentGatewayResolver gatewayResolver;
    private final BookingService bookingService;

    public PaymentService(PaymentRepository paymentRepository,
                          BookingRepository bookingRepository,
                          PaymentGatewayResolver gatewayResolver,
                          BookingService bookingService) {
        this.paymentRepository = paymentRepository;
        this.bookingRepository = bookingRepository;
        this.gatewayResolver = gatewayResolver;
        this.bookingService = bookingService;
    }

    @Transactional
    public PaymentResponse processPayment(PaymentRequest request) {
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new BookingNotFoundException(request.getBookingId()));

        // Guard: only allow payments for non-terminal bookings
        if (booking.getStatus() == BookingStatus.CANCELLED
                || booking.getStatus() == BookingStatus.COMPLETED) {
            throw new PaymentProcessingException(
                    "Cannot accept payment for a booking in status: " + booking.getStatus());
        }

        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setAmount(request.getAmount());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setPaymentType(request.getPaymentType());
        payment.setPaymentStatus(PaymentStatus.PENDING);

        PaymentGateway gateway = gatewayResolver.resolve(request.getPaymentMethod());

        try {
            String txnRef = gateway.process(request, request.getAmount());
            payment.markSuccessful(txnRef);
        } catch (PaymentProcessingException ex) {
            payment.markFailed();
            paymentRepository.save(payment);
            throw ex;
        }

        Payment saved = paymentRepository.save(payment);
        booking.addPayment(saved);

        // Business rule: successful DEPOSIT or FULL payment while on hold → confirm booking
        if ((request.getPaymentType() == PaymentType.DEPOSIT
                || request.getPaymentType() == PaymentType.FULL)
                && booking.getStatus() == BookingStatus.PROVISIONAL_HOLD) {
            bookingService.confirmBooking(booking.getBookingId());
        }

        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<PaymentResponse> getPaymentsForBooking(Integer bookingId) {
        if (!bookingRepository.existsById(bookingId)) {
            throw new BookingNotFoundException(bookingId);
        }
        return paymentRepository.findByBooking_BookingId(bookingId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PaymentResponse getPayment(Integer paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentProcessingException("Payment not found: " + paymentId));
        return toResponse(payment);
    }

    private PaymentResponse toResponse(Payment p) {
        PaymentResponse r = new PaymentResponse();
        r.setPaymentId(p.getPaymentId());
        r.setBookingId(p.getBooking().getBookingId());
        r.setAmount(p.getAmount());
        r.setPaymentMethod(p.getPaymentMethod());
        r.setPaymentType(p.getPaymentType());
        r.setPaymentStatus(p.getPaymentStatus());
        r.setTransactionReference(p.getTransactionReference());
        r.setPaidAt(p.getPaidAt());
        return r;
    }
}
