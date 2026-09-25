package com.eventbridge.user.service;

import com.eventbridge.user.dto.UserResponse;
import com.eventbridge.user.entity.Person;
import com.eventbridge.user.entity.Role;
import com.eventbridge.user.exception.UserNotFoundException;
import com.eventbridge.user.repository.AdminRepository;
import com.eventbridge.user.repository.CoordinatorRepository;
import com.eventbridge.user.repository.CustomerRepository;
import com.eventbridge.user.repository.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Admin-facing account administration: listing accounts by role and
 * activating/suspending them. Only four roles exist for this project (a
 * fixed, small, closed set per the requirement analysis), so a direct
 * switch over the four repositories here is simpler and just as
 * maintainable as another strategy layer -- adding a fifth role class of
 * account is a rare enough change that the extra abstraction isn't
 * justified yet (YAGNI/KISS).
 */
@Service
@RequiredArgsConstructor
public class UserManagementService {

    private final CustomerRepository customerRepository;
    private final VendorRepository vendorRepository;
    private final CoordinatorRepository coordinatorRepository;
    private final AdminRepository adminRepository;

    public List<UserResponse> listByRole(Role role) {
        return findAllByRole(role).stream().map(UserResponse::from).toList();
    }

    @Transactional
    public UserResponse updateStatus(Role role, Integer id, String status) {
        Person person = findByIdAndRole(role, id);
        person.setStatus(status.toUpperCase());
        return UserResponse.from(person);
    }

    private List<? extends Person> findAllByRole(Role role) {
        return switch (role) {
            case CUSTOMER -> customerRepository.findAll();
            case VENDOR -> vendorRepository.findAll();
            case COORDINATOR -> coordinatorRepository.findAll();
            case ADMIN -> adminRepository.findAll();
        };
    }

    private Person findByIdAndRole(Role role, Integer id) {
        return switch (role) {
            case CUSTOMER -> customerRepository.findById(id).map(Person.class::cast)
                    .orElseThrow(() -> new UserNotFoundException(id));
            case VENDOR -> vendorRepository.findById(id).map(Person.class::cast)
                    .orElseThrow(() -> new UserNotFoundException(id));
            case COORDINATOR -> coordinatorRepository.findById(id).map(Person.class::cast)
                    .orElseThrow(() -> new UserNotFoundException(id));
            case ADMIN -> adminRepository.findById(id).map(Person.class::cast)
                    .orElseThrow(() -> new UserNotFoundException(id));
        };
    }
}
