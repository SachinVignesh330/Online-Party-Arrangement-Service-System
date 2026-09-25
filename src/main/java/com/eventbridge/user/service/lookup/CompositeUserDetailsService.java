package com.eventbridge.user.service.lookup;

import com.eventbridge.user.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Spring Security only ever talks to one {@link UserDetailsService}. This
 * implementation fans out across every {@link UserLookupStrategy} Spring has
 * registered and returns the first match, so a single email/password login
 * form transparently authenticates a Customer, Admin, Vendor or
 * Coordinator without the caller specifying which.
 */
@Service
@RequiredArgsConstructor
public class CompositeUserDetailsService implements UserDetailsService {

    private final List<UserLookupStrategy> strategies;

    @Override
    public UserDetails loadUserByUsername(String email) {
        return strategies.stream()
                .flatMap(strategy -> strategy.findByEmail(email).stream())
                .findFirst()
                .map(UserPrincipal::new)
                .orElseThrow(() -> new UsernameNotFoundException("No account found with email: " + email));
    }
}
