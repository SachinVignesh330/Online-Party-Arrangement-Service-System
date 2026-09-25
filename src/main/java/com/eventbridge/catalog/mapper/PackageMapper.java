package com.eventbridge.catalog.mapper;

import com.eventbridge.catalog.dto.PackageRequest;
import com.eventbridge.catalog.dto.PackageResponse;
import com.eventbridge.catalog.entity.Package;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Converts between Package entity and DTOs.
 *
 * SOLID – SRP: mapping logic lives here only — not in the service or controller.
 * DRY: toResponse() is reused for single and list responses.
 */
@Component
public class PackageMapper {

    /** Entity → Response DTO */
    public PackageResponse toResponse(Package pkg) {
        PackageResponse res = new PackageResponse();
        res.setPackageId(pkg.getPackageId());
        res.setPackageName(pkg.getPackageName());
        res.setDescription(pkg.getDescription());
        res.setDuration(pkg.getDuration());
        res.setGuestLimit(pkg.getGuestLimit());
        res.setBasePrice(pkg.getBasePrice());
        res.setStatus(pkg.getStatus());
        res.setTier(pkg.getTier());

        // Map associated add-ons to lightweight summaries
        List<PackageResponse.AddOnSummary> addOnSummaries = pkg.getAddOns().stream()
                .map(a -> new PackageResponse.AddOnSummary(a.getAddonId(), a.getName(), a.getPrice()))
                .collect(Collectors.toList());
        res.setAddOns(addOnSummaries);

        return res;
    }

    /** Request DTO → New Entity (for create operations) */
    public Package toEntity(PackageRequest req) {
        return new Package(
                req.getPackageName(),
                req.getDescription(),
                req.getDuration(),
                req.getGuestLimit(),
                req.getBasePrice(),
                req.getTier()
        );
    }

    /** Apply updated fields from a request DTO onto an existing entity (for update operations) */
    public void updateEntity(Package pkg, PackageRequest req) {
        pkg.setPackageName(req.getPackageName());
        pkg.setDescription(req.getDescription());
        pkg.setDuration(req.getDuration());
        pkg.setGuestLimit(req.getGuestLimit());
        pkg.setBasePrice(req.getBasePrice());
        pkg.setTier(req.getTier());
    }

    /** List of entities → List of response DTOs */
    public List<PackageResponse> toResponseList(List<Package> packages) {
        return packages.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
