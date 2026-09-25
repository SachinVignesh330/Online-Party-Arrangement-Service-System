package com.eventbridge.user.service;

import com.eventbridge.user.dto.AuthResponse;
import com.eventbridge.user.dto.LoginRequest;
import com.eventbridge.user.dto.RegisterRequest;
import com.eventbridge.user.dto.UserResponse;
import com.eventbridge.user.security.JwtService;
import com.eventbridge.user.security.UserPrincipal;
import com.eventbridge.user.service.lookup.CompositeUserDetailsService;
import com.eventbridge.user.service.registration.RegistrationStrategy;
import com.eventbridge.user.service.registration.RegistrationStrategyFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

/**
 * The single entry point the controllers talk to. It stays thin on purpose:
 * registration is delegated to the role-specific {@link RegistrationStrategy}
 * (via the factory), and authentication is delegated to Spring Security's
 * own {@link AuthenticationManager} rather than reimplementing password
 * checking here.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final RegistrationStrategyFactory registrationStrategyFactory;
    private final AuthenticationManager authenticationManager;
    private final CompositeUserDetailsService userDetailsService;
    private final JwtService jwtService;

    public UserResponse register(RegisterRequest request) {
        RegistrationStrategy strategy = registrationStrategyFactory.getStrategy(request.getRole());
        return strategy.register(request);
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        UserPrincipal principal = (UserPrincipal) userDetailsService.loadUserByUsername(request.getEmail());
        String token = jwtService.generateToken(principal);

        return AuthResponse.builder()
                .token(token)
                .role(principal.getRole())
                .name(principal.getPerson().getName())
                .email(principal.getUsername())
                .build();
    }
}
