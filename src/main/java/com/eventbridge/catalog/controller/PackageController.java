package com.eventbridge.catalog.controller;

import com.eventbridge.catalog.dto.PackageRequest;
import com.eventbridge.catalog.dto.PackageResponse;
import com.eventbridge.catalog.enums.PackageStatus;
import com.eventbridge.catalog.service.PackageService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST endpoints for Package catalogue management.
 *
 * Base URL: /api/packages
 *
 * SOLID – SRP: only handles HTTP input/output; all logic in PackageService.
 *         DIP: depends on PackageService interface, not the impl.
 *
 * KISS: each endpoint maps to exactly one service call.
 */
@RestController
@RequestMapping("/api/packages")
public class PackageController {

    private final PackageService packageService;

    public PackageController(PackageService packageService) {
        this.packageService = packageService;
    }

    // POST /api/packages — Admin creates a new package
    @PostMapping
    public ResponseEntity<PackageResponse> createPackage(
            @Valid @RequestBody PackageRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(packageService.createPackage(request));
    }

    // GET /api/packages — Get all packages (admin view)
    @GetMapping
    public ResponseEntity<List<PackageResponse>> getAllPackages() {
        return ResponseEntity.ok(packageService.getAllPackages());
    }

    // GET /api/packages/active — Customer-facing catalogue (ACTIVE only)
    @GetMapping("/active")
    public ResponseEntity<List<PackageResponse>> getActivePackages() {
        return ResponseEntity.ok(packageService.getPackagesByStatus(PackageStatus.ACTIVE));
    }

    // GET /api/packages/{id} — Get one package by ID
    @GetMapping("/{id}")
    public ResponseEntity<PackageResponse> getPackageById(@PathVariable Integer id) {
        return ResponseEntity.ok(packageService.getPackageById(id));
    }

    // PUT /api/packages/{id} — Admin updates a package
    @PutMapping("/{id}")
    public ResponseEntity<PackageResponse> updatePackage(
            @PathVariable Integer id,
            @Valid @RequestBody PackageRequest request) {
        return ResponseEntity.ok(packageService.updatePackage(id, request));
    }

    // PATCH /api/packages/{id}/activate — Admin activates a package
    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activatePackage(@PathVariable Integer id) {
        packageService.activatePackage(id);
        return ResponseEntity.noContent().build();
    }

    // PATCH /api/packages/{id}/deactivate — Admin hides a package
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivatePackage(@PathVariable Integer id) {
        packageService.deactivatePackage(id);
        return ResponseEntity.noContent().build();
    }

    // PATCH /api/packages/{id}/archive — Admin soft-deletes a package
    @PatchMapping("/{id}/archive")
    public ResponseEntity<Void> archivePackage(@PathVariable Integer id) {
        packageService.archivePackage(id);
        return ResponseEntity.noContent().build();
    }
}
