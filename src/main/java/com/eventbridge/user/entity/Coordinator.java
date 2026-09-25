package com.eventbridge.user.entity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "coordinator")
@AttributeOverride(name = "id", column = @Column(name = "coordinator_id"))
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@SuperBuilder
public class Coordinator extends Person {

    @Override
    public Role getRole() {
        return Role.COORDINATOR;
    }
}
