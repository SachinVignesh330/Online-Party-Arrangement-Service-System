package com.eventbridge.catalog.service;

import com.eventbridge.catalog.dto.AddOnRequest;
import com.eventbridge.catalog.dto.AddOnResponse;
import com.eventbridge.catalog.entity.AddOn;
import com.eventbridge.catalog.enums.AddOnStatus;
import com.eventbridge.catalog.exception.ResourceNotFoundException;
import com.eventbridge.catalog.mapper.AddOnMapper;
import com.eventbridge.catalog.repository.AddOnRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Business logic for the Add-on catalogue.
 * Same principles applied as PackageServiceImpl.
 */
@Service
@Transactional
public class AddOnServiceImpl implements AddOnService {

    private final AddOnRepository addOnRepository;
    private final AddOnMapper     addOnMapper;

    public AddOnServiceImpl(AddOnRepository addOnRepository, AddOnMapper addOnMapper) {
        this.addOnRepository = addOnRepository;
        this.addOnMapper     = addOnMapper;
    }

    @Override
    public AddOnResponse createAddOn(AddOnRequest request) {
        AddOn saved = addOnRepository.save(addOnMapper.toEntity(request));
        return addOnMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public AddOnResponse getAddOnById(Integer id) {
        return addOnMapper.toResponse(fetchOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AddOnResponse> getAllActiveAddOns() {
        return addOnMapper.toResponseList(addOnRepository.findByStatus(AddOnStatus.ACTIVE));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AddOnResponse> getAddOnsForPackage(Integer packageId) {
        return addOnMapper.toResponseList(
                addOnRepository.findByStatusAndPackages_PackageId(AddOnStatus.ACTIVE, packageId)
        );
    }

    @Override
    public AddOnResponse updateAddOn(Integer id, AddOnRequest request) {
        AddOn addOn = fetchOrThrow(id);
        addOnMapper.updateEntity(addOn, request);
        return addOnMapper.toResponse(addOnRepository.save(addOn));
    }

    @Override
    public void deactivateAddOn(Integer id) {
        AddOn addOn = fetchOrThrow(id);
        addOn.deactivate();
        addOnRepository.save(addOn);
    }

    @Override
    public void archiveAddOn(Integer id) {
        AddOn addOn = fetchOrThrow(id);
        addOn.archive();
        addOnRepository.save(addOn);
    }

    private AddOn fetchOrThrow(Integer id) {
        return addOnRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AddOn", id));
    }
}
