package com.xink.training.taskmanagement.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.LocalDateTime;

@Embeddable
public class AuditInfo {
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column(name = "created_by")
    private String createdBy;
    
    @Column(name = "updated_by")
    private String updatedBy;
    
    public void recordCreation(String userId) {
        this.createdAt = LocalDateTime.now();
        this.createdBy = userId;
    }
    
    public void recordUpdate(String userId) {
        this.updatedAt = LocalDateTime.now();
        this.updatedBy = userId;
    }
    
    public void recordUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public String getCreatedBy() {
        return createdBy;
    }
    
    public String getUpdatedBy() {
        return updatedBy;
    }
}

