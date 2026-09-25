package com.eventbridge.user.dto;

import com.eventbridge.user.entity.Person;
import com.eventbridge.user.entity.Role;
import lombok.Builder;
import lombok.Getter;

/**
 * Safe, outward-facing view of any {@link Person} subclass -- never exposes
 * the password hash. {@link #from} is a small static factory so every
 * caller builds this the same way regardless of which concrete role entity
 * (Customer/Admin/Vendor/Coordinator) it came from.
 */
@Getter
@Builder
public class UserResponse {
    private Integer id;
    private String name;
    private String email;
    private String phone;
    private Role role;
    private String status;

    public static UserResponse from(Person person) {
        return UserResponse.builder()
                .id(person.getId())
                .name(person.getName())
                .email(person.getEmail())
                .phone(person.getPhone())
                .role(person.getRole())
                .status(person.getStatus())
                .build();
    }
}
