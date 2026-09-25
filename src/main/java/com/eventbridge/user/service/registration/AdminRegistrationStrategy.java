package com.eventbridge.user.service.registration;

import com.eventbridge.user.dto.RegisterRequest;
import com.eventbridge.user.entity.Admin;
import com.eventbridge.user.entity.Person;
import com.eventbridge.user.entity.Role;
import com.eventbridge.user.repository.AdminRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Registering a new Admin/Owner is intentionally still exposed through the
 * same {@code /api/auth/register} endpoint as the other roles for now
 * (YAGNI -- no separate "provision an admin" workflow was asked for). In a
 * production rollout this would likely be locked down to existing admins
 * only; see the README for the suggested follow-up.
 */
@Component
public class AdminRegistrationStrategy extends AbstractRegistrationStrategy {

    private final AdminRepository adminRepository;

    public AdminRegistrationStrategy(PasswordEncoder passwordEncoder, AdminRepository adminRepository) {
        super(passwordEncoder);
        this.adminRepository = adminRepository;
    }

    @Override
    public Role getSupportedRole() {
        return Role.ADMIN;
    }

    @Override
    protected boolean emailExists(String email) {
        return adminRepository.existsByEmail(email);
    }

    @Override
    protected Person buildEntity(RegisterRequest request) {
        return Admin.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .status("ACTIVE")
                .build();
    }

    @Override
    protected Person persist(Person entity) {
        return adminRepository.save((Admin) entity);
    }
}
