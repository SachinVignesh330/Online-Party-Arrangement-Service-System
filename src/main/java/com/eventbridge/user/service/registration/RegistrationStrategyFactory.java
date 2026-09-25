package com.eventbridge.user.service.registration;

import com.eventbridge.user.entity.Role;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Factory Method: hands {@link com.eventbridge.user.service.AuthService} the
 * right {@link RegistrationStrategy} for a given {@link Role}. Spring
 * collects every {@code @Component} that implements the interface into the
 * list below, so wiring a new role in only means adding a new strategy bean
 * -- this factory never has to be touched (Open/Closed Principle).
 */
@Component
public class RegistrationStrategyFactory {

    private final Map<Role, RegistrationStrategy> strategiesByRole;

    public RegistrationStrategyFactory(List<RegistrationStrategy> strategies) {
        this.strategiesByRole = strategies.stream()
                .collect(Collectors.toMap(RegistrationStrategy::getSupportedRole, Function.identity()));
    }

    public RegistrationStrategy getStrategy(Role role) {
        RegistrationStrategy strategy = strategiesByRole.get(role);
        if (strategy == null) {
            throw new IllegalArgumentException("No registration strategy configured for role: " + role);
        }
        return strategy;
    }
}
