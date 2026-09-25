package com.eventbridge.booking.domain.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "booking_add_on")
public class BookingAddOn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_addon_id")
    private Integer bookingAddonId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @Column(name = "addon_id", nullable = false)
    private Integer addonId;

    @Column(name = "quantity")
    private Integer quantity = 1;

    @Column(name = "unit_price", precision = 12, scale = 2)
    private BigDecimal unitPrice;

    public BigDecimal getLineTotal() {
        if (unitPrice == null || quantity == null) {
            return BigDecimal.ZERO;
        }
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    // ---- Getters / Setters ----

    public Integer getBookingAddonId() {
        return bookingAddonId;
    }

    public void setBookingAddonId(Integer bookingAddonId) {
        this.bookingAddonId = bookingAddonId;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookingAddOn that = (BookingAddOn) o;
        return bookingAddonId != null && Objects.equals(bookingAddonId, that.bookingAddonId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
