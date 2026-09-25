package com.eventbridge.eventday.checklist;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "checklist_item")
public class ChecklistItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "checklist_item_id")
    private Integer checklistItemId;

    @Column(name = "booking_id")
    private Integer bookingId;

    @Column(name = "task_name")
    private String taskName;

    private String description;

    @Column(name = "is_completed")
    private Boolean isCompleted = false;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    public ChecklistItem() {
    }

    public Integer getChecklistItemId() {
        return checklistItemId;
    }

    public void setChecklistItemId(Integer checklistItemId) {
        this.checklistItemId = checklistItemId;
    }

    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }
}
