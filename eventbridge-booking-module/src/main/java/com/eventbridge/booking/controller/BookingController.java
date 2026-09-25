package com.eventbridge.booking.controller;

import com.eventbridge.booking.dto.request.CreateBookingRequest;
import com.eventbridge.booking.dto.response.BookingResponse;
import com.eventbridge.booking.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<BookingResponse> create(@Valid @RequestBody CreateBookingRequest request) {
        BookingResponse created = bookingService.createBooking(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(bookingService.getBooking(id));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<BookingResponse>> getByCustomer(@PathVariable Integer customerId) {
        return ResponseEntity.ok(bookingService.getBookingsByCustomer(customerId));
    }

    /** Inquiry → Provisional Hold (default 48 h) */
    @PostMapping("/{id}/hold")
    public ResponseEntity<BookingResponse> placeHold(@PathVariable Integer id) {
        return ResponseEntity.ok(bookingService.placeHold(id));
    }

    /** Explicit confirm (normally triggered by successful payment) */
    @PostMapping("/{id}/confirm")
    public ResponseEntity<BookingResponse> confirm(@PathVariable Integer id) {
        return ResponseEntity.ok(bookingService.confirmBooking(id));
    }

    @PostMapping("/{id}/start")
    public ResponseEntity<BookingResponse> startProgress(@PathVariable Integer id) {
        return ResponseEntity.ok(bookingService.startProgress(id));
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<BookingResponse> complete(@PathVariable Integer id) {
        return ResponseEntity.ok(bookingService.completeBooking(id));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<BookingResponse> cancel(@PathVariable Integer id) {
        return ResponseEntity.ok(bookingService.cancelBooking(id));
    }
}
