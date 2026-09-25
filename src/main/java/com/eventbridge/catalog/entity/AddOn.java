package com.eventbridge.catalog.entity;

import com.eventbridge.catalog.enums.AddOnStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * An optional extra that a customer can attach to any package booking.
 *
 * Examples: extra hours (Rs.5,000/hr), additional lighting rig (Rs.1,500),
 *           live food station (Rs.15,000), upgraded sound system (Rs.5,000).
 *
 * OOP – encapsulation: status transitions are domain methods, not raw setters.
 * SOLID – SRP: owns add-on catalogue data only; price computation delegated
 *              to PricingStrategy in the service layer.
 */
@Entity
@Table(name = "add_on")
public class AddOn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "addon_id")
    private Integer addonId;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @NotNull
    @DecimalMin("0.00")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    /**
     * Free-text constraint from the business spec, e.g. "Any tier",
     * "Standard, Premium".  Kept as a human-readable label; structural
     * enforcement is handled in the service layer via the ManyToMany
     * package relationship.
     */
    @Size(max = 100)
    @Column(name = "applies_to", length = 100)
    private String appliesTo;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private AddOnStatus status = AddOnStatus.ACTIVE;

    @ManyToMany(mappedBy = "addOns", fetch = FetchType.LAZY)
    private List<Package> packages = new ArrayList<>();

    // ------------------------------------------------------------------ //
    //  Domain behaviour                                                     //
    // ------------------------------------------------------------------ //

    public void activate()   { this.status = AddOnStatus.ACTIVE; }
    public void deactivate() { this.status = AddOnStatus.INACTIVE; }
    public void archive()    { this.status = AddOnStatus.ARCHIVED; }

    public boolean isAvailable() {
        return this.status == AddOnStatus.ACTIVE;
    }

    // ------------------------------------------------------------------ //
    //  Constructors                                                         //
    // ------------------------------------------------------------------ //

    protected AddOn() { /* JPA */ }

    public AddOn(String name, String description, BigDecimal price, String appliesTo) {
        this.name        = name;
        this.description = description;
        this.price       = price;
        this.appliesTo   = appliesTo;
        this.status      = AddOnStatus.ACTIVE;
    }

    // ------------------------------------------------------------------ //
    //  Getters / Setters                                                    //
    // ------------------------------------------------------------------ //

    public Integer      getAddonId()              { return addonId; }
    public String       getName()                 { return name; }
    public void         setName(String n)         { this.name = n; }
    public String       getDescription()          { return description; }
    public void         setDescription(String d)  { this.description = d; }
    public BigDecimal   getPrice()                { return price; }
    public void         setPrice(BigDecimal p)    { this.price = p; }
    public String       getAppliesTo()            { return appliesTo; }
    public void         setAppliesTo(String a)    { this.appliesTo = a; }
    public AddOnStatus  getStatus()               { return status; }
    public List<Package> getPackages()            { return packages; }
}
