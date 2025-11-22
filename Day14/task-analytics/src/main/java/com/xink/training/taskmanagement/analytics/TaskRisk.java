package com.xink.training.taskmanagement.analytics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskRisk {
    private UUID taskId;
    private String taskTitle;
    private String riskType;
    private String description;
    private double riskLevel;
}

