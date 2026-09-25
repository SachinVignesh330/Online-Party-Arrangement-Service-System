package com.eventbridge.catalog.controller;

import com.eventbridge.catalog.dto.AddOnRequest;
import com.eventbridge.catalog.dto.AddOnResponse;
import com.eventbridge.catalog.service.AddOnService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST endpoints for Add-on catalogue management.
 * Base URL: /api/addons
 */
@RestController
@RequestMapping("/api/addons")
public class AddOnController {

    private final AddOnService addOnService;

    public AddOnController(AddOnService addOnService) {
        this.addOnService = addOnService;
    }

    // POST /api/addons — Admin creates a new add-on
    @PostMapping
    public ResponseEntity<AddOnResponse> createAddOn(
            @Valid @RequestBody AddOnRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(addOnService.createAddOn(request));
    }

    // GET /api/addons — All active add-ons
    @GetMapping
    public ResponseEntity<List<AddOnResponse>> getAllActiveAddOns() {
        return ResponseEntity.ok(addOnService.getAllActiveAddOns());
    }

    // GET /api/addons/{id}
    @GetMapping("/{id}")
    public ResponseEntity<AddOnResponse> getAddOnById(@PathVariable Integer id) {
        return ResponseEntity.ok(addOnService.getAddOnById(id));
    }

    // GET /api/addons/package/{packageId} — Add-ons compatible with a specific package
    @GetMapping("/package/{packageId}")
    public ResponseEntity<List<AddOnResponse>> getAddOnsForPackage(
            @PathVariable Integer packageId) {
        return ResponseEntity.ok(addOnService.getAddOnsForPackage(packageId));
    }

    // PUT /api/addons/{id} — Admin updates an add-on
    @PutMapping("/{id}")
    public ResponseEntity<AddOnResponse> updateAddOn(
            @PathVariable Integer id,
            @Valid @RequestBody AddOnRequest request) {
        return ResponseEntity.ok(addOnService.updateAddOn(id, request));
    }

    // PATCH /api/addons/{id}/deactivate
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateAddOn(@PathVariable Integer id) {
        addOnService.deactivateAddOn(id);
        return ResponseEntity.noContent().build();
    }

    // PATCH /api/addons/{id}/archive
    @PatchMapping("/{id}/archive")
    public ResponseEntity<Void> archiveAddOn(@PathVariable Integer id) {
        addOnService.archiveAddOn(id);
        return ResponseEntity.noContent().build();
    }
}
