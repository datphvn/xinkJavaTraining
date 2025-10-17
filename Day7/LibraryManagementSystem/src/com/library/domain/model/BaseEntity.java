package com.library.domain.model;

import com.library.domain.exception.ValidationException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Abstract base class for all domain entities.
 * Provides common functionality like ID, audit information, and soft delete.
 * Pure OOP implementation without any framework dependencies.
 */
public abstract class BaseEntity {
    protected UUID id;
    protected LocalDateTime createdAt;
    protected LocalDateTime updatedAt;
    protected String createdBy;
    protected String updatedBy;
    protected long version;
    protected boolean deleted;
    protected LocalDateTime deletedAt;

    /**
     * Constructor for creating a new entity.
     */
    protected BaseEntity() {
        this.id = UUID.randomUUID();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.version = 0;
        this.deleted = false;
    }

    // Getters
    public UUID getId() {
        return id;
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

    public long getVersion() {
        return version;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    // Setters for audit information
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        this.updatedAt = LocalDateTime.now();
        this.version++;
    }

    // Business methods

    /**
     * Marks the entity as deleted (soft delete).
     */
    public void markAsDeleted() {
        this.deleted = true;
        this.deletedAt = LocalDateTime.now();
        this.version++;
    }

    /**
     * Restores a soft-deleted entity.
     */
    public void restore() {
        this.deleted = false;
        this.deletedAt = null;
        this.version++;
    }

    /**
     * Validates the entity's state.
     * @throws ValidationException if the entity is in an invalid state
     */
    public abstract void validate() throws ValidationException;

    /**
     * Converts the entity to a map representation.
     * @return A map containing the entity's data
     */
    public abstract Map<String, Object> toMap();

    /**
     * Populates the entity from a map representation.
     * @param data The map containing the entity's data
     */
    public abstract void fromMap(Map<String, Object> data);

    /**
     * Gets a summary of the entity for display purposes.
     * @return A string summary of the entity
     */
    public abstract String getSummary();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseEntity)) return false;
        BaseEntity that = (BaseEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", deleted=" + deleted +
                '}';
    }
}
