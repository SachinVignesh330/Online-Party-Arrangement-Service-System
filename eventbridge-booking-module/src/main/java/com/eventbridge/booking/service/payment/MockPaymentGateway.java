package com.eventbridge.booking.service.payment;

import com.eventbridge.booking.domain.enums.PaymentMethod;
import com.eventbridge.booking.dto.request.PaymentRequest;
import com.eventbridge.booking.exception.PaymentProcessingException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Development / test gateway. Always succeeds unless amount is zero or negative.
 * Replace with a real PCI-compliant gateway implementation in production.
 */
@Component
public class MockPaymentGateway implements PaymentGateway {

    @Override
    public String process(PaymentRequest request, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new PaymentProcessingException("Invalid payment amount");
        }
        // Simulate gateway latency / success
        return "MOCK-TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    @Override
    public boolean supports(PaymentMethod method) {
        // Mock supports everything for development convenience
        return true;
    }
}
