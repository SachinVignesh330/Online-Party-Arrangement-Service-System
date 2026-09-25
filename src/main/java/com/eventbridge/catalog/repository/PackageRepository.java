package com.eventbridge.catalog.repository;

import com.eventbridge.catalog.entity.Package;
import com.eventbridge.catalog.enums.PackageStatus;
import com.eventbridge.catalog.enums.PackageTier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Data access for Package entities.
 *
 * SOLID – ISP / SRP: only declares methods this module needs.
 *         Cross-module callers (e.g. Booking module) receive a Package object
 *         through the service layer, never directly through this repository.
 *
 * KISS: Spring Data JPA generates the SQL — no boilerplate query code needed.
 */
@Repository
public interface PackageRepository extends JpaRepository<Package, Integer> {

    /** Returns only bookable packages for the customer catalogue. */
    List<Package> findByStatus(PackageStatus status);

    /** Used by the Booking module to verify coordinator-assignment rules by tier. */
    List<Package> findByTier(PackageTier tier);

    boolean existsByPackageNameIgnoreCase(String packageName);
}
