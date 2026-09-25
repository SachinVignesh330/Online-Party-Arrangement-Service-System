package com.eventbridge.user.repository;

import com.eventbridge.user.entity.Coordinator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CoordinatorRepository extends JpaRepository<Coordinator, Integer> {
    Optional<Coordinator> findByEmail(String email);
    boolean existsByEmail(String email);
}
