package com.eventbridge.booking.domain.entity;

import com.eventbridge.booking.domain.enums.BookingStatus;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Core aggregate root for the Booking & Payment module.
 * Owns BookingAddOn children and is referenced by Payment.
 * Lifecycle transitions are mediated by the State pattern (see domain.state).
 */
@Entity
@Table(name = "booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private Integer bookingId;

    @Column(name = "customer_id", nullable = false)
    private Integer customerId;

    @Column(name = "package_id", nullable = false)
    private Integer packageId;

    @Column(name = "coordinator_id")
    private Integer coordinatorId;

    @Column(name = "event_date", nullable = false)
    private LocalDate eventDate;

    @Column(name = "location", length = 255)
    private String location;

    @Column(name = "guest_count")
    private Integer guestCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 30)
    private BookingStatus status;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "hold_until")
    private LocalDateTime holdUntil;

    @Column(name = "confirmed_at")
    private LocalDateTime confirmedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<BookingAddOn> addOns = new ArrayList<>();

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Payment> payments = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (status == null) {
            status = BookingStatus.INQUIRY;
        }
    }

    // ---- Domain behaviour (keeps business rules close to the data) ----

    public void addAddOn(BookingAddOn addOn) {
        addOns.add(addOn);
        addOn.setBooking(this);
    }

    public void removeAddOn(BookingAddOn addOn) {
        addOns.remove(addOn);
        addOn.setBooking(null);
    }

    public void addPayment(Payment payment) {
        payments.add(payment);
        payment.setBooking(this);
    }

    public boolean isHoldExpired() {
        return status == BookingStatus.PROVISIONAL_HOLD
                && holdUntil != null
                && LocalDateTime.now().isAfter(holdUntil);
    }

    // ---- Getters / Setters ----

    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getPackageId() {
        return packageId;
    }

    public void setPackageId(Integer packageId) {
        this.packageId = packageId;
    }

    public Integer getCoordinatorId() {
        return coordinatorId;
    }

    public void setCoordinatorId(Integer coordinatorId) {
        this.coordinatorId = coordinatorId;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getGuestCount() {
        return guestCount;
    }

    public void setGuestCount(Integer guestCount) {
        this.guestCount = guestCount;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getHoldUntil() {
        return holdUntil;
    }

    public void setHoldUntil(LocalDateTime holdUntil) {
        this.holdUntil = holdUntil;
    }

    public LocalDateTime getConfirmedAt() {
        return confirmedAt;
    }

    public void setConfirmedAt(LocalDateTime confirmedAt) {
        this.confirmedAt = confirmedAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public List<BookingAddOn> getAddOns() {
        return addOns;
    }

    public void setAddOns(List<BookingAddOn> addOns) {
        this.addOns = addOns;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return bookingId != null && Objects.equals(bookingId, booking.bookingId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
