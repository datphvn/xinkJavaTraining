package com.xink.training.taskmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskUpdateResult {
    private UUID taskId;
    private boolean success;
    private String errorMessage;
    
    public static TaskUpdateResult success(UUID taskId) {
        return TaskUpdateResult.builder()
            .taskId(taskId)
            .success(true)
            .build();
    }
    
    public static TaskUpdateResult failure(UUID taskId, String errorMessage) {
        return TaskUpdateResult.builder()
            .taskId(taskId)
            .success(false)
            .errorMessage(errorMessage)
            .build();
    }
}

