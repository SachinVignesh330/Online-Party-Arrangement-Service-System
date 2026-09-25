package com.eventbridge.user.entity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Entity
@Table(name = "vendor")
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "vendor_id")),
        @AttributeOverride(name = "name", column = @Column(name = "vendor_name"))
})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@SuperBuilder
public class Vendor extends Person {

    /** e.g. Caterer, DJ/Entertainment, Decorator, Photographer. */
    private String category;

    @Column(name = "performance_rating")
    private BigDecimal performanceRating;

    @Override
    public Role getRole() {
        return Role.VENDOR;
    }
}
