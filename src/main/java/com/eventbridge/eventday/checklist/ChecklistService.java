package com.eventbridge.eventday.checklist;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChecklistService {

    private final ChecklistItemRepository checklistItemRepository;

    public ChecklistService(ChecklistItemRepository checklistItemRepository) {
        this.checklistItemRepository = checklistItemRepository;
    }

    /**
     * Generates the day-of checklist for a booking based on its package tier.
     * tier is expected to be "Basic", "Standard", or "Premium".
     */
    public List<ChecklistItem> generateForBooking(Integer bookingId, String tier) {
        List<ChecklistItem> items = new ArrayList<>();

        items.add(newItem(bookingId, "Confirm venue access", "Verify delivery window access"));
        items.add(newItem(bookingId, "Deliver core inventory", "Tents/lighting/sound/furniture per package"));

        if (!"Basic".equalsIgnoreCase(tier)) {
            items.add(newItem(bookingId, "Confirm caterer arrival", "Match headcount + dietary breakdown"));
        }
        if ("Standard".equalsIgnoreCase(tier) || "Premium".equalsIgnoreCase(tier)) {
            items.add(newItem(bookingId, "Confirm DJ/entertainment setup", "Time-slot + equipment check"));
        }
        if ("Premium".equalsIgnoreCase(tier)) {
            items.add(newItem(bookingId, "Confirm photographer arrival", "Cross-check event timeline"));
            items.add(newItem(bookingId, "Dedicated coordinator full-day check-in", "Present entire duration"));
        }

        items.add(newItem(bookingId, "Teardown & pickup", "Schedule inventory pickup after event"));

        return checklistItemRepository.saveAll(items);
    }

    public List<ChecklistItem> getForBooking(Integer bookingId) {
        return checklistItemRepository.findByBookingId(bookingId);
    }

    public ChecklistItem markComplete(Integer itemId) {
        ChecklistItem item = checklistItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Checklist item not found"));
        item.setIsCompleted(true);
        item.setCompletedAt(LocalDateTime.now());
        return checklistItemRepository.save(item);
    }

    private ChecklistItem newItem(Integer bookingId, String task, String description) {
        ChecklistItem item = new ChecklistItem();
        item.setBookingId(bookingId);
        item.setTaskName(task);
        item.setDescription(description);
        item.setIsCompleted(false);
        return item;
    }
}
