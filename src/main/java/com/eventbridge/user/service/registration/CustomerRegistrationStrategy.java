package com.eventbridge.user.service.registration;

import com.eventbridge.user.dto.RegisterRequest;
import com.eventbridge.user.entity.Customer;
import com.eventbridge.user.entity.Person;
import com.eventbridge.user.entity.Role;
import com.eventbridge.user.repository.CustomerRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomerRegistrationStrategy extends AbstractRegistrationStrategy {

    private final CustomerRepository customerRepository;

    public CustomerRegistrationStrategy(PasswordEncoder passwordEncoder, CustomerRepository customerRepository) {
        super(passwordEncoder);
        this.customerRepository = customerRepository;
    }

    @Override
    public Role getSupportedRole() {
        return Role.CUSTOMER;
    }

    @Override
    protected boolean emailExists(String email) {
        return customerRepository.existsByEmail(email);
    }

    @Override
    protected Person buildEntity(RegisterRequest request) {
        return Customer.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .status("ACTIVE")
                .build();
    }

    @Override
    protected Person persist(Person entity) {
        return customerRepository.save((Customer) entity);
    }
}
