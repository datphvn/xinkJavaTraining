package com.xink.training.taskmanagement.domain;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Converter
public class TaskStatusConverter implements AttributeConverter<TaskStatus, String> {
    private final ObjectMapper objectMapper = new ObjectMapper()
        .registerModule(new JavaTimeModule());
    
    @Override
    public String convertToDatabaseColumn(TaskStatus status) {
        if (status == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(status);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting TaskStatus to JSON", e);
        }
    }
    
    @Override
    public TaskStatus convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return new TaskStatus.Todo();
        }
        try {
            // Simple implementation - in production, use proper deserialization
            if (dbData.contains("Todo")) {
                return new TaskStatus.Todo();
            } else if (dbData.contains("InProgress")) {
                return new TaskStatus.InProgress();
            } else if (dbData.contains("Review")) {
                return new TaskStatus.Review("system");
            } else if (dbData.contains("Done")) {
                return new TaskStatus.Done();
            } else if (dbData.contains("Cancelled")) {
                return new TaskStatus.Cancelled("System");
            }
            return new TaskStatus.Todo();
        } catch (Exception e) {
            throw new RuntimeException("Error converting JSON to TaskStatus", e);
        }
    }
}

