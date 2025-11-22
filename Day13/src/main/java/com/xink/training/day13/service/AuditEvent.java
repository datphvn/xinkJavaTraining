package com.xink.training.day13.service;

public class AuditEvent {
    private final String action;
    private final String entityType;
    
    public AuditEvent(String action, String entityType) {
        this.action = action;
        this.entityType = entityType;
    }
    
    public String getAction() {
        return action;
    }
    
    public String getEntityType() {
        return entityType;
    }
}

