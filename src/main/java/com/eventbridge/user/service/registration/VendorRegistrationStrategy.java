package com.eventbridge.user.service.registration;

import com.eventbridge.user.dto.RegisterRequest;
import com.eventbridge.user.entity.Person;
import com.eventbridge.user.entity.Role;
import com.eventbridge.user.entity.Vendor;
import com.eventbridge.user.repository.VendorRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class VendorRegistrationStrategy extends AbstractRegistrationStrategy {

    private final VendorRepository vendorRepository;

    public VendorRegistrationStrategy(PasswordEncoder passwordEncoder, VendorRepository vendorRepository) {
        super(passwordEncoder);
        this.vendorRepository = vendorRepository;
    }

    @Override
    public Role getSupportedRole() {
        return Role.VENDOR;
    }

    @Override
    protected boolean emailExists(String email) {
        return vendorRepository.existsByEmail(email);
    }

    @Override
    protected Person buildEntity(RegisterRequest request) {
        return Vendor.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .category(request.getCategory())
                .status("ACTIVE")
                .build();
    }

    @Override
    protected Person persist(Person entity) {
        return vendorRepository.save((Vendor) entity);
    }
}
