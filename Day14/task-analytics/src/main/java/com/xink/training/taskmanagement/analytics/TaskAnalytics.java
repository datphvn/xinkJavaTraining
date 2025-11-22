package com.xink.training.taskmanagement.analytics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.Period;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskAnalytics {
    private int totalTasks;
    private long completedTasks;
    private long inProgressTasks;
    private long overdueTasks;
    private double averageCompletionTime;
    private Map<String, Long> tasksByPriority;
    private Map<String, Long> tasksByAssignee;
    private List<ProductivityTrend> productivityTrends;
    private List<ContributorStats> topContributors;
}

