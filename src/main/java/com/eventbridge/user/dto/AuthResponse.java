package com.eventbridge.user.dto;

import com.eventbridge.user.entity.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthResponse {
    private String token;
    private Role role;
    private String name;
    private String email;
}
