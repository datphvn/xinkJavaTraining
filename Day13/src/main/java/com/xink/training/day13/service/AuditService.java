package com.xink.training.day13.service;

public interface AuditService {
    void logEvent(AuditEvent event);
    void logCriticalEvent(AuditEvent event);
}

