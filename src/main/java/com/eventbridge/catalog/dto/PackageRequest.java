package com.eventbridge.catalog.dto;

import com.eventbridge.catalog.enums.PackageTier;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/**
 * Inbound DTO for creating or updating a Package.
 *
 * SOLID – SRP: carries only data; no business logic.
 * YAGNI: no versioning or audit fields — those are the entity's concern.
 */
public class PackageRequest {

    @NotBlank(message = "Package name is required")
    @Size(max = 100)
    private String packageName;

    private String description;

    @DecimalMin(value = "0.0", message = "Duration must be positive")
    private BigDecimal duration;

    @Min(value = 1, message = "Guest limit must be at least 1")
    private Integer guestLimit;

    @NotNull(message = "Base price is required")
    @DecimalMin(value = "0.00", message = "Base price must be non-negative")
    private BigDecimal basePrice;

    @NotNull(message = "Package tier is required")
    private PackageTier tier;

    // ------------------------------------------------------------------ //
    //  Getters / Setters                                                   //
    // ------------------------------------------------------------------ //

    public String      getPackageName()             { return packageName; }
    public void        setPackageName(String n)     { this.packageName = n; }
    public String      getDescription()             { return description; }
    public void        setDescription(String d)     { this.description = d; }
    public BigDecimal  getDuration()                { return duration; }
    public void        setDuration(BigDecimal d)    { this.duration = d; }
    public Integer     getGuestLimit()              { return guestLimit; }
    public void        setGuestLimit(Integer g)     { this.guestLimit = g; }
    public BigDecimal  getBasePrice()               { return basePrice; }
    public void        setBasePrice(BigDecimal p)   { this.basePrice = p; }
    public PackageTier getTier()                    { return tier; }
    public void        setTier(PackageTier t)       { this.tier = t; }
}
