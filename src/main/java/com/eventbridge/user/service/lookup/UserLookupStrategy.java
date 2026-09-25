package com.eventbridge.user.service.lookup;

import com.eventbridge.user.entity.Person;

import java.util.Optional;

/**
 * Strategy interface: one implementation per role knows how to find its own
 * kind of account by email. {@link CompositeUserDetailsService} depends only
 * on this abstraction (DIP) and doesn't know or care how many roles exist,
 * so a fifth role is added by writing one more implementation -- no
 * existing class changes (Open/Closed Principle).
 */
public interface UserLookupStrategy {
    Optional<Person> findByEmail(String email);
}
