package com.eventbridge.booking.service;

import com.eventbridge.booking.domain.entity.Booking;
import com.eventbridge.booking.domain.entity.BookingAddOn;
import com.eventbridge.booking.domain.enums.BookingStatus;
import com.eventbridge.booking.domain.enums.PaymentStatus;
import com.eventbridge.booking.domain.state.BookingState;
import com.eventbridge.booking.domain.state.BookingStateFactory;
import com.eventbridge.booking.dto.request.CreateBookingRequest;
import com.eventbridge.booking.dto.response.BookingResponse;
import com.eventbridge.booking.exception.BookingNotFoundException;
import com.eventbridge.booking.repository.BookingRepository;
import com.eventbridge.booking.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Application service orchestrating the booking lifecycle.
 * Uses the State Pattern for transitions and keeps transaction boundaries explicit.
 */
@Service
public class BookingService {

    public static final int DEFAULT_HOLD_HOURS = 48;

    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;
    private final PricingService pricingService;
    // In a full system: InventoryReservationService, VendorAssignmentService would be injected here

    public BookingService(BookingRepository bookingRepository,
                          PaymentRepository paymentRepository,
                          PricingService pricingService) {
        this.bookingRepository = bookingRepository;
        this.paymentRepository = paymentRepository;
        this.pricingService = pricingService;
    }

    @Transactional
    public BookingResponse createBooking(CreateBookingRequest request) {
        Booking booking = new Booking();
        booking.setCustomerId(request.getCustomerId());
        booking.setPackageId(request.getPackageId());
        booking.setEventDate(request.getEventDate());
        booking.setLocation(request.getLocation());
        booking.setGuestCount(request.getGuestCount());
        booking.setStatus(BookingStatus.INQUIRY);

        if (request.getAddOns() != null) {
            for (CreateBookingRequest.AddOnRequest ao : request.getAddOns()) {
                BookingAddOn addOn = new BookingAddOn();
                addOn.setAddonId(ao.getAddonId());
                addOn.setQuantity(ao.getQuantity() != null ? ao.getQuantity() : 1);
                // unitPrice should be resolved from Catalog module; left null for now
                // (caller / Catalog integration sets it before persist in production)
                booking.addAddOn(addOn);
            }
        }

        Booking saved = bookingRepository.save(booking);
        return toResponse(saved);
    }

    /**
     * Moves Inquiry → Provisional Hold (48 h by default).
     * In production this is the point where Inventory & Vendor modules lock resources.
     */
    @Transactional
    public BookingResponse placeHold(Integer bookingId) {
        return placeHold(bookingId, DEFAULT_HOLD_HOURS);
    }

    @Transactional
    public BookingResponse placeHold(Integer bookingId, int holdHours) {
        Booking booking = findOrThrow(bookingId);
        BookingState state = BookingStateFactory.from(booking.getStatus());
        state.placeHold(booking, holdHours);
        return toResponse(bookingRepository.save(booking));
    }

    /**
     * Called after a successful deposit/full payment.
     * Moves Provisional Hold → Confirmed.
     */
    @Transactional
    public BookingResponse confirmBooking(Integer bookingId) {
        Booking booking = findOrThrow(bookingId);
        BookingState state = BookingStateFactory.from(booking.getStatus());
        state.confirm(booking);
        return toResponse(bookingRepository.save(booking));
    }

    @Transactional
    public BookingResponse startProgress(Integer bookingId) {
        Booking booking = findOrThrow(bookingId);
        BookingState state = BookingStateFactory.from(booking.getStatus());
        state.startProgress(booking);
        return toResponse(bookingRepository.save(booking));
    }

    @Transactional
    public BookingResponse completeBooking(Integer bookingId) {
        Booking booking = findOrThrow(bookingId);
        BookingState state = BookingStateFactory.from(booking.getStatus());
        state.complete(booking);
        return toResponse(bookingRepository.save(booking));
    }

    @Transactional
    public BookingResponse cancelBooking(Integer bookingId) {
        Booking booking = findOrThrow(bookingId);
        BookingState state = BookingStateFactory.from(booking.getStatus());
        state.cancel(booking);
        // Side-effect: release inventory / vendor slots would be triggered here
        return toResponse(bookingRepository.save(booking));
    }

    /**
     * Scheduled job entry-point: release expired provisional holds.
     */
    @Transactional
    public int releaseExpiredHolds() {
        List<Booking> expired = bookingRepository.findExpiredHolds(
                BookingStatus.PROVISIONAL_HOLD, LocalDateTime.now());
        for (Booking b : expired) {
            BookingState state = BookingStateFactory.from(b.getStatus());
            state.cancel(b); // cancel releases the hold
            bookingRepository.save(b);
        }
        return expired.size();
    }

    @Transactional(readOnly = true)
    public BookingResponse getBooking(Integer bookingId) {
        return toResponse(findOrThrow(bookingId));
    }

    @Transactional(readOnly = true)
    public List<BookingResponse> getBookingsByCustomer(Integer customerId) {
        return bookingRepository.findByCustomerId(customerId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ---- helpers ----

    private Booking findOrThrow(Integer bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException(bookingId));
    }

    private BookingResponse toResponse(Booking booking) {
        BookingResponse r = new BookingResponse();
        r.setBookingId(booking.getBookingId());
        r.setCustomerId(booking.getCustomerId());
        r.setPackageId(booking.getPackageId());
        r.setCoordinatorId(booking.getCoordinatorId());
        r.setEventDate(booking.getEventDate());
        r.setLocation(booking.getLocation());
        r.setGuestCount(booking.getGuestCount());
        r.setStatus(booking.getStatus());
        r.setCreatedAt(booking.getCreatedAt());
        r.setHoldUntil(booking.getHoldUntil());
        r.setConfirmedAt(booking.getConfirmedAt());
        r.setCompletedAt(booking.getCompletedAt());

        r.setAddOns(booking.getAddOns().stream().map(ao -> {
            BookingResponse.AddOnResponse ar = new BookingResponse.AddOnResponse();
            ar.setBookingAddonId(ao.getBookingAddonId());
            ar.setAddonId(ao.getAddonId());
            ar.setQuantity(ao.getQuantity());
            ar.setUnitPrice(ao.getUnitPrice());
            ar.setLineTotal(ao.getLineTotal());
            return ar;
        }).collect(Collectors.toList()));

        BigDecimal totalPaid = paymentRepository.sumSuccessfulPayments(
                booking.getBookingId(), PaymentStatus.SUCCESS);
        r.setTotalPaid(totalPaid != null ? totalPaid : BigDecimal.ZERO);

        // estimatedTotal left null unless Catalog module supplies package base price
        r.setEstimatedTotal(pricingService.calculateAddOnTotal(booking));

        return r;
    }
}
