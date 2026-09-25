package com.eventbridge.catalog.enums;

/**
 * Represents the four tiers defined in the EventBridge business profile.
 *
 * BASIC    — up to ~50 guests, 4-hour window, delivery crew only.
 * STANDARD — up to ~120 guests, 6-hour window, part-time coordinator.
 * PREMIUM  — up to ~300 guests, full-day, dedicated coordinator.
 * CUSTOM   — quote-based; routed through the Vendor & Bidding Module.
 *
 * The tier drives coordinator-assignment rules in the Booking & Payment Module
 * (STANDARD → partial check-in, PREMIUM → full-day coordinator, BASIC → none).
 */
public enum PackageTier {
    BASIC,
    STANDARD,
    PREMIUM,
    CUSTOM
}
