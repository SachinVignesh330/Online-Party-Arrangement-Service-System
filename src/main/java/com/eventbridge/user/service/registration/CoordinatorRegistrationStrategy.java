package com.eventbridge.user.service.registration;

import com.eventbridge.user.dto.RegisterRequest;
import com.eventbridge.user.entity.Coordinator;
import com.eventbridge.user.entity.Person;
import com.eventbridge.user.entity.Role;
import com.eventbridge.user.repository.CoordinatorRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CoordinatorRegistrationStrategy extends AbstractRegistrationStrategy {

    private final CoordinatorRepository coordinatorRepository;

    public CoordinatorRegistrationStrategy(PasswordEncoder passwordEncoder,
                                            CoordinatorRepository coordinatorRepository) {
        super(passwordEncoder);
        this.coordinatorRepository = coordinatorRepository;
    }

    @Override
    public Role getSupportedRole() {
        return Role.COORDINATOR;
    }

    @Override
    protected boolean emailExists(String email) {
        return coordinatorRepository.existsByEmail(email);
    }

    @Override
    protected Person buildEntity(RegisterRequest request) {
        return Coordinator.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .status("ACTIVE")
                .build();
    }

    @Override
    protected Person persist(Person entity) {
        return coordinatorRepository.save((Coordinator) entity);
    }
}
