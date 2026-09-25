package com.eventbridge.user.service.lookup;

import com.eventbridge.user.entity.Person;
import com.eventbridge.user.repository.CoordinatorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CoordinatorLookupStrategy implements UserLookupStrategy {

    private final CoordinatorRepository coordinatorRepository;

    @Override
    public Optional<Person> findByEmail(String email) {
        return coordinatorRepository.findByEmail(email).map(Person.class::cast);
    }
}
