package com.eventbridge.user.service.registration;

import com.eventbridge.user.dto.RegisterRequest;
import com.eventbridge.user.dto.UserResponse;
import com.eventbridge.user.entity.Role;

/**
 * Strategy interface: one implementation per role owns how that role turns
 * a {@link RegisterRequest} into a persisted account. {@link
 * RegistrationStrategyFactory} picks the right one by {@link #getSupportedRole()}.
 */
public interface RegistrationStrategy {
    Role getSupportedRole();
    UserResponse register(RegisterRequest request);
}
