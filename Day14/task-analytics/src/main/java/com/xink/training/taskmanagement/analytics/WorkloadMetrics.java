package com.xink.training.taskmanagement.analytics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkloadMetrics {
    private long assignedTasks;
    private long completedTasks;
    private double completionRate;
    private Duration totalEstimatedWork;
    private Duration totalActualWork;
    private long overdueTasks;
    private double averagePriorityLevel;
}

