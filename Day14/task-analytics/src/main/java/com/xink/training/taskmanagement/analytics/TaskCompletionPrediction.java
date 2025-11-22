package com.xink.training.taskmanagement.analytics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskCompletionPrediction {
    private UUID taskId;
    private String taskTitle;
    private String currentStatus;
    private LocalDateTime predictedCompletionDate;
    private double confidence;
    private boolean isLikelyToBeOverdue;
}

