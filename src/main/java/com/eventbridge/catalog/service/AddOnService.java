package com.eventbridge.catalog.service;

import com.eventbridge.catalog.dto.AddOnRequest;
import com.eventbridge.catalog.dto.AddOnResponse;

import java.util.List;

/**
 * Service contract for the Add-on catalogue.
 */
public interface AddOnService {

    AddOnResponse         createAddOn(AddOnRequest request);
    AddOnResponse         getAddOnById(Integer id);
    List<AddOnResponse>   getAllActiveAddOns();
    List<AddOnResponse>   getAddOnsForPackage(Integer packageId);
    AddOnResponse         updateAddOn(Integer id, AddOnRequest request);
    void                  deactivateAddOn(Integer id);
    void                  archiveAddOn(Integer id);
}
