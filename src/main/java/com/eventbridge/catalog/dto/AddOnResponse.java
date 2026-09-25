package com.eventbridge.catalog.dto;

import com.eventbridge.catalog.enums.AddOnStatus;
import java.math.BigDecimal;

public class AddOnResponse {

    private Integer     addonId;
    private String      name;
    private String      description;
    private BigDecimal  price;
    private String      appliesTo;
    private AddOnStatus status;

    public Integer     getAddonId()              { return addonId; }
    public void        setAddonId(Integer id)    { this.addonId = id; }
    public String      getName()                 { return name; }
    public void        setName(String n)         { this.name = n; }
    public String      getDescription()          { return description; }
    public void        setDescription(String d)  { this.description = d; }
    public BigDecimal  getPrice()                { return price; }
    public void        setPrice(BigDecimal p)    { this.price = p; }
    public String      getAppliesTo()            { return appliesTo; }
    public void        setAppliesTo(String a)    { this.appliesTo = a; }
    public AddOnStatus getStatus()               { return status; }
    public void        setStatus(AddOnStatus s)  { this.status = s; }
}
