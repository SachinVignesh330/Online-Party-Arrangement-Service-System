package com.eventbridge.catalog.entity;

import com.eventbridge.catalog.enums.PackageStatus;
import com.eventbridge.catalog.enums.PackageTier;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents one of EventBridge's service packages (Basic / Standard / Premium / Custom).
 *
 * OOP: encapsulates all package-level data and exposes behaviour through
 *      domain methods (activate, deactivate, archive) rather than raw setters,
 *      preventing invalid state transitions from outside the object.
 *
 * SOLID – SRP: this class owns package data only; pricing calculation lives in
 *              PricingStrategy so the two can vary independently.
 */
@Entity
@Table(name = "package")
public class Package {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "package_id")
    private Integer packageId;

    @NotBlank
    @Size(max = 100)
    @Column(name = "package_name", nullable = false, length = 100)
    private String packageName;

    @Column(columnDefinition = "TEXT")
    private String description;

    /**
     * Total event window in hours (e.g. 4.0 for Basic, 6.0 for Standard).
     * Stored as DECIMAL(5,2) to match the DB schema.
     */
    @DecimalMin("0.0")
    @Column(precision = 5, scale = 2)
    private BigDecimal duration;

    @Min(1)
    @Column(name = "guest_limit")
    private Integer guestLimit;

    @NotNull
    @DecimalMin("0.00")
    @Column(name = "base_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal basePrice;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private PackageStatus status = PackageStatus.ACTIVE;

    /**
     * Tier drives coordinator-assignment rules in the Booking module.
     * Not persisted as a separate column — derived from package_name convention —
     * but stored explicitly here so downstream modules can query it without
     * string-matching on the name.
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private PackageTier tier;

    /**
     * Bi-directional convenience: a Package knows which Add-ons apply to it.
     * Managed via the join table; Add-on owns the applicable_to field.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "package_addon",
            joinColumns = @JoinColumn(name = "package_id"),
            inverseJoinColumns = @JoinColumn(name = "addon_id")
    )
    private List<AddOn> addOns = new ArrayList<>();

    // ------------------------------------------------------------------ //
    //  Domain behaviour — state transitions (OOP encapsulation)           //
    // ------------------------------------------------------------------ //

    /** Makes the package visible in the customer catalogue. */
    public void activate() {
        this.status = PackageStatus.ACTIVE;
    }

    /** Hides the package without destroying historical booking data. */
    public void deactivate() {
        this.status = PackageStatus.INACTIVE;
    }

    /**
     * Soft-deletes the package.
     * Once archived a package cannot be reactivated (business rule).
     */
    public void archive() {
        this.status = PackageStatus.ARCHIVED;
    }

    public boolean isBookable() {
        return this.status == PackageStatus.ACTIVE;
    }

    // ------------------------------------------------------------------ //
    //  Constructors                                                        //
    // ------------------------------------------------------------------ //

    protected Package() { /* JPA requires no-arg constructor */ }

    public Package(String packageName, String description, BigDecimal duration,
                   Integer guestLimit, BigDecimal basePrice, PackageTier tier) {
        this.packageName = packageName;
        this.description = description;
        this.duration    = duration;
        this.guestLimit  = guestLimit;
        this.basePrice   = basePrice;
        this.tier        = tier;
        this.status      = PackageStatus.ACTIVE;
    }

    // ------------------------------------------------------------------ //
    //  Getters / Setters                                                   //
    // ------------------------------------------------------------------ //

    public Integer getPackageId()            { return packageId; }
    public String  getPackageName()          { return packageName; }
    public void    setPackageName(String n)  { this.packageName = n; }
    public String  getDescription()          { return description; }
    public void    setDescription(String d)  { this.description = d; }
    public BigDecimal getDuration()          { return duration; }
    public void    setDuration(BigDecimal d) { this.duration = d; }
    public Integer getGuestLimit()           { return guestLimit; }
    public void    setGuestLimit(Integer g)  { this.guestLimit = g; }
    public BigDecimal getBasePrice()         { return basePrice; }
    public void    setBasePrice(BigDecimal p){ this.basePrice = p; }
    public PackageStatus getStatus()         { return status; }
    public PackageTier   getTier()           { return tier; }
    public void    setTier(PackageTier t)    { this.tier = t; }
    public List<AddOn>   getAddOns()         { return addOns; }
}
