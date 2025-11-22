package com.xink.training.taskmanagement.analytics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PerformanceMetrics {
    private double completionRate;
    private double averageVelocity;
    private double averageLeadTime;
    private double averageCycleTime;
    private double defectRate;
    private long totalTasks;
}

