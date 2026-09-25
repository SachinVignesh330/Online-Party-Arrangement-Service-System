package com.eventbridge.booking.service.payment;

import com.eventbridge.booking.domain.enums.PaymentMethod;
import com.eventbridge.booking.dto.request.PaymentRequest;

import java.math.BigDecimal;

/**
 * Strategy interface for payment processing.
 * Concrete implementations can be swapped (Stripe, Razorpay, mock, etc.)
 * without changing the rest of the module (Open/Closed + Dependency Inversion).
 */
public interface PaymentGateway {

    /**
     * @return gateway transaction reference on success
     * @throws com.eventbridge.booking.exception.PaymentProcessingException on failure
     */
    String process(PaymentRequest request, BigDecimal amount);

    boolean supports(PaymentMethod method);
}
