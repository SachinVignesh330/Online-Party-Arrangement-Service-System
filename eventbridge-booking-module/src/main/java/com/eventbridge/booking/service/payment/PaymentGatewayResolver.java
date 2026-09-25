package com.eventbridge.booking.service.payment;

import com.eventbridge.booking.domain.enums.PaymentMethod;
import com.eventbridge.booking.exception.PaymentProcessingException;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Selects the appropriate PaymentGateway strategy at runtime.
 */
@Component
public class PaymentGatewayResolver {

    private final List<PaymentGateway> gateways;

    public PaymentGatewayResolver(List<PaymentGateway> gateways) {
        this.gateways = gateways;
    }

    public PaymentGateway resolve(PaymentMethod method) {
        return gateways.stream()
                .filter(g -> g.supports(method))
                .findFirst()
                .orElseThrow(() -> new PaymentProcessingException(
                        "No payment gateway available for method: " + method));
    }
}
