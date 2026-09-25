package com.eventbridge.user.service.lookup;

import com.eventbridge.user.entity.Person;
import com.eventbridge.user.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomerLookupStrategy implements UserLookupStrategy {

    private final CustomerRepository customerRepository;

    @Override
    public Optional<Person> findByEmail(String email) {
        return customerRepository.findByEmail(email).map(Person.class::cast);
    }
}
