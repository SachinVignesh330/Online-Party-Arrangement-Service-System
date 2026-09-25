package com.eventbridge.user.repository;

import com.eventbridge.user.entity.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VendorRepository extends JpaRepository<Vendor, Integer> {
    Optional<Vendor> findByEmail(String email);
    boolean existsByEmail(String email);
}
