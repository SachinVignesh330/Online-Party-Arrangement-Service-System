package com.eventbridge.user.entity;

/**
 * The four actors defined in the requirement analysis: Customer, Admin/Owner,
 * Vendor and Event Coordinator. Adding a fifth role later means adding one
 * enum value plus one lookup/registration strategy -- nothing here has to
 * change (Open/Closed Principle).
 */
public enum Role {
    CUSTOMER,
    ADMIN,
    VENDOR,
    COORDINATOR
}
