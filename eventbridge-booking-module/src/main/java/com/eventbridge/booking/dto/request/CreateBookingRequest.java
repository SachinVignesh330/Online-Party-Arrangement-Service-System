package com.eventbridge.booking.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CreateBookingRequest {

    @NotNull(message = "Customer ID is required")
    private Integer customerId;

    @NotNull(message = "Package ID is required")
    private Integer packageId;

    @NotNull(message = "Event date is required")
    @FutureOrPresent(message = "Event date cannot be in the past")
    private LocalDate eventDate;

    @Size(max = 255)
    private String location;

    @Min(value = 1, message = "Guest count must be at least 1")
    private Integer guestCount;

    @Valid
    private List<AddOnRequest> addOns = new ArrayList<>();

    // ---- Getters / Setters ----

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

    public List<AddOnRequest> getAddOns() {
        return addOns;
    }

    public void setAddOns(List<AddOnRequest> addOns) {
        this.addOns = addOns;
    }

    public static class AddOnRequest {
        @NotNull
        private Integer addonId;

        @Min(1)
        private Integer quantity = 1;

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
    }
}
