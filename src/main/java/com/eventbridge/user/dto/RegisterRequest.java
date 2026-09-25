package com.eventbridge.user.dto;

import com.eventbridge.user.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * One shared registration payload for all four roles. Which fields matter
 * (e.g. {@code category} only applies to a Vendor) is decided by whichever
 * {@link com.eventbridge.user.service.registration.RegistrationStrategy}
 * handles the given {@link #role} -- this DTO stays dumb on purpose (KISS).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be a valid address")
    private String email;

    private String phone;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @NotNull(message = "Role is required")
    private Role role;

    /** Only used when role = VENDOR (e.g. Caterer, DJ, Decorator, Photographer). */
    private String category;
}
