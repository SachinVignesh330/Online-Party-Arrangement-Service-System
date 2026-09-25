package com.eventbridge.catalog.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * Returned by the live price calculator endpoint.
 * Shows the customer a full price breakdown before they confirm booking.
 */
public class PriceQuoteResponse {

    private Integer     packageId;
    private String      packageName;
    private String      pricingLabel;       // e.g. "Loyalty pricing (10% discount applied)"
    private BigDecimal  basePrice;
    private BigDecimal  cateringTotal;      // cateringPerHead × guestCount
    private BigDecimal  addOnTotal;
    private BigDecimal  grandTotal;
    private List<String> appliedAddOns;     // names of selected add-ons

    // ------------------------------------------------------------------ //
    //  Getters / Setters                                                   //
    // ------------------------------------------------------------------ //

    public Integer     getPackageId()                    { return packageId; }
    public void        setPackageId(Integer id)          { this.packageId = id; }
    public String      getPackageName()                  { return packageName; }
    public void        setPackageName(String n)          { this.packageName = n; }
    public String      getPricingLabel()                 { return pricingLabel; }
    public void        setPricingLabel(String l)         { this.pricingLabel = l; }
    public BigDecimal  getBasePrice()                    { return basePrice; }
    public void        setBasePrice(BigDecimal p)        { this.basePrice = p; }
    public BigDecimal  getCateringTotal()                { return cateringTotal; }
    public void        setCateringTotal(BigDecimal c)    { this.cateringTotal = c; }
    public BigDecimal  getAddOnTotal()                   { return addOnTotal; }
    public void        setAddOnTotal(BigDecimal a)       { this.addOnTotal = a; }
    public BigDecimal  getGrandTotal()                   { return grandTotal; }
    public void        setGrandTotal(BigDecimal g)       { this.grandTotal = g; }
    public List<String> getAppliedAddOns()               { return appliedAddOns; }
    public void        setAppliedAddOns(List<String> a)  { this.appliedAddOns = a; }
}
