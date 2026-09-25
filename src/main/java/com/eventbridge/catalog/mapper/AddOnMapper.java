package com.eventbridge.catalog.mapper;

import com.eventbridge.catalog.dto.AddOnRequest;
import com.eventbridge.catalog.dto.AddOnResponse;
import com.eventbridge.catalog.entity.AddOn;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Converts between AddOn entity and DTOs.
 */
@Component
public class AddOnMapper {

    public AddOnResponse toResponse(AddOn addOn) {
        AddOnResponse res = new AddOnResponse();
        res.setAddonId(addOn.getAddonId());
        res.setName(addOn.getName());
        res.setDescription(addOn.getDescription());
        res.setPrice(addOn.getPrice());
        res.setAppliesTo(addOn.getAppliesTo());
        res.setStatus(addOn.getStatus());
        return res;
    }

    public AddOn toEntity(AddOnRequest req) {
        return new AddOn(
                req.getName(),
                req.getDescription(),
                req.getPrice(),
                req.getAppliesTo()
        );
    }

    public void updateEntity(AddOn addOn, AddOnRequest req) {
        addOn.setName(req.getName());
        addOn.setDescription(req.getDescription());
        addOn.setPrice(req.getPrice());
        addOn.setAppliesTo(req.getAppliesTo());
    }

    public List<AddOnResponse> toResponseList(List<AddOn> addOns) {
        return addOns.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
