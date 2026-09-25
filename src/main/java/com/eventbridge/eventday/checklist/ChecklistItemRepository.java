package com.eventbridge.eventday.checklist;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChecklistItemRepository extends JpaRepository<ChecklistItem, Integer> {
    List<ChecklistItem> findByBookingId(Integer bookingId);
}
