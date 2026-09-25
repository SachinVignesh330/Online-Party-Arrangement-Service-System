package com.eventbridge.eventday.checklist;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings/{bookingId}/checklist")
public class ChecklistController {

    private final ChecklistService checklistService;

    public ChecklistController(ChecklistService checklistService) {
        this.checklistService = checklistService;
    }

    // POST /api/bookings/5/checklist/generate?tier=Premium
    @PostMapping("/generate")
    public List<ChecklistItem> generate(@PathVariable Integer bookingId, @RequestParam String tier) {
        return checklistService.generateForBooking(bookingId, tier);
    }

    @GetMapping
    public List<ChecklistItem> get(@PathVariable Integer bookingId) {
        return checklistService.getForBooking(bookingId);
    }

    @PatchMapping("/{itemId}/complete")
    public ChecklistItem complete(@PathVariable Integer bookingId, @PathVariable Integer itemId) {
        return checklistService.markComplete(itemId);
    }
}
