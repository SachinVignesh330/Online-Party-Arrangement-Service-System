package com.eventbridge.catalog.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class AddOnRequest {

    @NotBlank(message = "Add-on name is required")
    @Size(max = 100)
    private String name;

    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.00", message = "Price must be non-negative")
    private BigDecimal price;

    @Size(max = 100)
    private String appliesTo;

    public String    getName()               { return name; }
    public void      setName(String n)       { this.name = n; }
    public String    getDescription()        { return description; }
    public void      setDescription(String d){ this.description = d; }
    public BigDecimal getPrice()             { return price; }
    public void      setPrice(BigDecimal p)  { this.price = p; }
    public String    getAppliesTo()          { return appliesTo; }
    public void      setAppliesTo(String a)  { this.appliesTo = a; }
}
