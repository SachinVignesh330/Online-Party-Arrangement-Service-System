package com.eventbridge.catalog.service;

import com.eventbridge.catalog.dto.PackageRequest;
import com.eventbridge.catalog.dto.PackageResponse;
import com.eventbridge.catalog.entity.Package;
import com.eventbridge.catalog.enums.PackageStatus;
import com.eventbridge.catalog.exception.DuplicatePackageException;
import com.eventbridge.catalog.exception.ResourceNotFoundException;
import com.eventbridge.catalog.mapper.PackageMapper;
import com.eventbridge.catalog.repository.PackageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Business logic for the Package catalogue.
 *
 * OOP  – Encapsulation: state transitions (activate/deactivate/archive)
 *         are delegated to the Package entity's own domain methods,
 *         not set directly from outside.
 *
 * SOLID – SRP: only Package business rules live here.
 *         OCP: new behaviour (e.g. bulk operations) added via new methods,
 *              not by modifying existing ones.
 *         DIP: depends on PackageRepository (interface) and PackageMapper,
 *              not on concrete JPA classes.
 *
 * DRY  – fetchOrThrow() is a single private helper used by every method
 *         that needs to load a package by ID.
 *
 * KISS – each method does exactly one thing; no nested conditionals.
 */
@Service
@Transactional
public class PackageServiceImpl implements PackageService {

    private final PackageRepository packageRepository;
    private final PackageMapper     packageMapper;

    // Constructor injection — preferred over @Autowired (easier to test)
    public PackageServiceImpl(PackageRepository packageRepository,
                              PackageMapper packageMapper) {
        this.packageRepository = packageRepository;
        this.packageMapper     = packageMapper;
    }

    // ------------------------------------------------------------------ //
    //  Create                                                              //
    // ------------------------------------------------------------------ //

    @Override
    public PackageResponse createPackage(PackageRequest request) {
        // Guard: no duplicate package names
        if (packageRepository.existsByPackageNameIgnoreCase(request.getPackageName())) {
            throw new DuplicatePackageException(request.getPackageName());
        }
        Package saved = packageRepository.save(packageMapper.toEntity(request));
        return packageMapper.toResponse(saved);
    }

    // ------------------------------------------------------------------ //
    //  Read                                                                //
    // ------------------------------------------------------------------ //

    @Override
    @Transactional(readOnly = true)
    public PackageResponse getPackageById(Integer id) {
        return packageMapper.toResponse(fetchOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PackageResponse> getAllPackages() {
        return packageMapper.toResponseList(packageRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PackageResponse> getPackagesByStatus(PackageStatus status) {
        return packageMapper.toResponseList(packageRepository.findByStatus(status));
    }

    // ------------------------------------------------------------------ //
    //  Update                                                              //
    // ------------------------------------------------------------------ //

    @Override
    public PackageResponse updatePackage(Integer id, PackageRequest request) {
        Package pkg = fetchOrThrow(id);

        // Check duplicate only if name is actually changing
        if (!pkg.getPackageName().equalsIgnoreCase(request.getPackageName())
                && packageRepository.existsByPackageNameIgnoreCase(request.getPackageName())) {
            throw new DuplicatePackageException(request.getPackageName());
        }

        packageMapper.updateEntity(pkg, request);
        return packageMapper.toResponse(packageRepository.save(pkg));
    }

    // ------------------------------------------------------------------ //
    //  Status transitions — delegate to entity domain methods (OOP)       //
    // ------------------------------------------------------------------ //

    @Override
    public void activatePackage(Integer id) {
        Package pkg = fetchOrThrow(id);
        pkg.activate();
        packageRepository.save(pkg);
    }

    @Override
    public void deactivatePackage(Integer id) {
        Package pkg = fetchOrThrow(id);
        pkg.deactivate();
        packageRepository.save(pkg);
    }

    @Override
    public void archivePackage(Integer id) {
        Package pkg = fetchOrThrow(id);
        pkg.archive();
        packageRepository.save(pkg);
    }

    // ------------------------------------------------------------------ //
    //  Private helper — DRY: single place to handle "not found"           //
    // ------------------------------------------------------------------ //

    private Package fetchOrThrow(Integer id) {
        return packageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Package", id));
    }
}
