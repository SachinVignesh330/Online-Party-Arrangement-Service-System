package com.eventbridge.catalog.repository;

import com.eventbridge.catalog.entity.AddOn;
import com.eventbridge.catalog.enums.AddOnStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Data access for AddOn entities.
 */
@Repository
public interface AddOnRepository extends JpaRepository<AddOn, Integer> {

    List<AddOn> findByStatus(AddOnStatus status);

    /** Fetches only add-ons that are active AND compatible with a given package. */
    List<AddOn> findByStatusAndPackages_PackageId(AddOnStatus status, Integer packageId);
}
