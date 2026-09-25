package com.eventbridge.user.controller;

import com.eventbridge.user.dto.UpdateStatusRequest;
import com.eventbridge.user.dto.UserResponse;
import com.eventbridge.user.entity.Role;
import com.eventbridge.user.service.UserManagementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Admin/Owner account administration: "Manage staff/coordinators", vendors
 * and customer accounts (list + activate/suspend). Method-level
 * {@code @PreAuthorize} plus the {@code /api/admin/**} matcher in
 * {@link com.eventbridge.user.security.SecurityConfig} enforce this in two
 * layers, matching the requirement that "each role sees different data and
 * can perform different actions."
 */
@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class UserManagementController {

    private final UserManagementService userManagementService;

    @GetMapping("/{role}")
    public ResponseEntity<List<UserResponse>> listByRole(@PathVariable Role role) {
        return ResponseEntity.ok(userManagementService.listByRole(role));
    }

    @PatchMapping("/{role}/{id}/status")
    public ResponseEntity<UserResponse> updateStatus(@PathVariable Role role,
                                                       @PathVariable Integer id,
                                                       @Valid @RequestBody UpdateStatusRequest request) {
        return ResponseEntity.ok(userManagementService.updateStatus(role, id, request.getStatus()));
    }
}
