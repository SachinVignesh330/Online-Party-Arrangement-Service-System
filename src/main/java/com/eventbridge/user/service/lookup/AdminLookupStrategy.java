package com.eventbridge.user.service.lookup;

import com.eventbridge.user.entity.Person;
import com.eventbridge.user.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AdminLookupStrategy implements UserLookupStrategy {

    private final AdminRepository adminRepository;

    @Override
    public Optional<Person> findByEmail(String email) {
        return adminRepository.findByEmail(email).map(Person.class::cast);
    }
}
