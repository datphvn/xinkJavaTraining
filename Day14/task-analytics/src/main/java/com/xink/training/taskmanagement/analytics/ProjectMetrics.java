package com.xink.training.taskmanagement.analytics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectMetrics {
    private long totalTasks;
    private long completedTasks;
    private double completionRate;
    private double averageTaskDuration;
}

