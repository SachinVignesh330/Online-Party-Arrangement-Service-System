package com.eventbridge.catalog.service;

import com.eventbridge.catalog.dto.PackageRequest;
import com.eventbridge.catalog.dto.PackageResponse;
import com.eventbridge.catalog.enums.PackageStatus;

import java.util.List;

/**
 * Service contract for the Package catalogue.
 *
 * SOLID – ISP: defines only what the Package domain needs.
 *         DIP: controllers depend on this interface, not the implementation,
 *              so the impl can be swapped or mocked in tests without
 *              touching the controller.
 */
public interface PackageService {

    PackageResponse  createPackage(PackageRequest request);
    PackageResponse  getPackageById(Integer id);
    List<PackageResponse> getAllPackages();
    List<PackageResponse> getPackagesByStatus(PackageStatus status);
    PackageResponse  updatePackage(Integer id, PackageRequest request);
    void             activatePackage(Integer id);
    void             deactivatePackage(Integer id);
    void             archivePackage(Integer id);
}
