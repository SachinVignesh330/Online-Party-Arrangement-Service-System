package com.eventbridge.catalog.enums;

/**
 * Represents the lifecycle state of a Package.
 * ACTIVE   — visible to customers; can be booked.
 * INACTIVE — hidden from the catalogue; existing bookings unaffected.
 * ARCHIVED — soft-deleted; kept for historical reporting only.
 */
public enum PackageStatus {
    ACTIVE,
    INACTIVE,
    ARCHIVED
}
