package com.eventbridge.user.service.registration;

import com.eventbridge.user.dto.RegisterRequest;
import com.eventbridge.user.dto.UserResponse;
import com.eventbridge.user.entity.Person;
import com.eventbridge.user.exception.UserAlreadyExistsException;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Template Method: {@link #register} fixes the algorithm every role follows
 * (reject duplicate email -> build the entity -> hash the password ->
 * persist -> return a safe response) so that logic is written exactly once
 * (DRY). Each concrete subclass only fills in the three role-specific hooks.
 */
public abstract class AbstractRegistrationStrategy implements RegistrationStrategy {

    protected final PasswordEncoder passwordEncoder;

    protected AbstractRegistrationStrategy(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public final UserResponse register(RegisterRequest request) {
        if (emailExists(request.getEmail())) {
            throw new UserAlreadyExistsException(request.getEmail());
        }

        Person entity = buildEntity(request);
        entity.setPassword(passwordEncoder.encode(request.getPassword()));

        Person saved = persist(entity);
        return UserResponse.from(saved);
    }

    /** Hook: does an account with this email already exist for this role? */
    protected abstract boolean emailExists(String email);

    /** Hook: build the (unsaved, unencoded-password) entity for this role. */
    protected abstract Person buildEntity(RegisterRequest request);

    /** Hook: persist the entity via this role's repository. */
    protected abstract Person persist(Person entity);
}
