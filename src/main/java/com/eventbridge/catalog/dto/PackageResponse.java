package com.eventbridge.catalog.dto;

import com.eventbridge.catalog.enums.PackageStatus;
import com.eventbridge.catalog.enums.PackageTier;

import java.math.BigDecimal;
import java.util.List;

/**
 * Outbound DTO — what the Presentation Layer receives.
 * Never exposes the JPA entity directly (protects the domain model).
 */
public class PackageResponse {

    private Integer       packageId;
    private String        packageName;
    private String        description;
    private BigDecimal    duration;
    private Integer       guestLimit;
    private BigDecimal    basePrice;
    private PackageStatus status;
    private PackageTier   tier;
    private List<AddOnSummary> addOns;

    /** Lightweight nested summary to avoid circular serialisation. */
    public record AddOnSummary(Integer addonId, String name, BigDecimal price) {}

    // ------------------------------------------------------------------ //
    //  Getters / Setters                                                   //
    // ------------------------------------------------------------------ //

    public Integer          getPackageId()                 { return packageId; }
    public void             setPackageId(Integer id)       { this.packageId = id; }
    public String           getPackageName()               { return packageName; }
    public void             setPackageName(String n)       { this.packageName = n; }
    public String           getDescription()               { return description; }
    public void             setDescription(String d)       { this.description = d; }
    public BigDecimal       getDuration()                  { return duration; }
    public void             setDuration(BigDecimal d)      { this.duration = d; }
    public Integer          getGuestLimit()                { return guestLimit; }
    public void             setGuestLimit(Integer g)       { this.guestLimit = g; }
    public BigDecimal       getBasePrice()                 { return basePrice; }
    public void             setBasePrice(BigDecimal p)     { this.basePrice = p; }
    public PackageStatus    getStatus()                    { return status; }
    public void             setStatus(PackageStatus s)     { this.status = s; }
    public PackageTier      getTier()                      { return tier; }
    public void             setTier(PackageTier t)         { this.tier = t; }
    public List<AddOnSummary> getAddOns()                  { return addOns; }
    public void             setAddOns(List<AddOnSummary> a){ this.addOns = a; }
}
