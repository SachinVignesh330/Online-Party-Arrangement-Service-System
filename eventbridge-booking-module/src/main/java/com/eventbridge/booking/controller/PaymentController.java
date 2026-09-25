package com.eventbridge.booking.controller;

import com.eventbridge.booking.dto.request.PaymentRequest;
import com.eventbridge.booking.dto.response.PaymentResponse;
import com.eventbridge.booking.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<PaymentResponse> process(@Valid @RequestBody PaymentRequest request) {
        PaymentResponse result = paymentService.processPayment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(paymentService.getPayment(id));
    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<List<PaymentResponse>> getByBooking(@PathVariable Integer bookingId) {
        return ResponseEntity.ok(paymentService.getPaymentsForBooking(bookingId));
    }
}
