package com.eventbridge.user.service.lookup;

import com.eventbridge.user.entity.Person;
import com.eventbridge.user.repository.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class VendorLookupStrategy implements UserLookupStrategy {

    private final VendorRepository vendorRepository;

    @Override
    public Optional<Person> findByEmail(String email) {
        return vendorRepository.findByEmail(email).map(Person.class::cast);
    }
}
