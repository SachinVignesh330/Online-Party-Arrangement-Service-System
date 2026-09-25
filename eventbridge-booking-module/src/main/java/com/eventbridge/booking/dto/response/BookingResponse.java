package com.eventbridge.booking.dto.response;

import com.eventbridge.booking.domain.enums.BookingStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class BookingResponse {

    private Integer bookingId;
    private Integer customerId;
    private Integer packageId;
    private Integer coordinatorId;
    private LocalDate eventDate;
    private String location;
    private Integer guestCount;
    private BookingStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime holdUntil;
    private LocalDateTime confirmedAt;
    private LocalDateTime completedAt;
    private List<AddOnResponse> addOns;
    private BigDecimal totalPaid;
    private BigDecimal estimatedTotal;

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

    public List<AddOnResponse> getAddOns() {
        return addOns;
    }

    public void setAddOns(List<AddOnResponse> addOns) {
        this.addOns = addOns;
    }

    public BigDecimal getTotalPaid() {
        return totalPaid;
    }

    public void setTotalPaid(BigDecimal totalPaid) {
        this.totalPaid = totalPaid;
    }

    public BigDecimal getEstimatedTotal() {
        return estimatedTotal;
    }

    public void setEstimatedTotal(BigDecimal estimatedTotal) {
        this.estimatedTotal = estimatedTotal;
    }

    public static class AddOnResponse {
        private Integer bookingAddonId;
        private Integer addonId;
        private Integer quantity;
        private BigDecimal unitPrice;
        private BigDecimal lineTotal;

        public Integer getBookingAddonId() {
            return bookingAddonId;
        }

        public void setBookingAddonId(Integer bookingAddonId) {
            this.bookingAddonId = bookingAddonId;
        }

        public Integer getAddonId() {
            return addonId;
        }

        public void setAddonId(Integer addonId) {
            this.addonId = addonId;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }

        public BigDecimal getUnitPrice() {
            return unitPrice;
        }

        public void setUnitPrice(BigDecimal unitPrice) {
            this.unitPrice = unitPrice;
        }

        public BigDecimal getLineTotal() {
            return lineTotal;
        }

        public void setLineTotal(BigDecimal lineTotal) {
            this.lineTotal = lineTotal;
        }
    }
}
