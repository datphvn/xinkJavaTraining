package com.xink.training.taskmanagement.analytics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProductivity {
    private long tasksCompleted;
    private Duration totalWorkTime;
    private double averageTaskCompletionTime;
    private double efficiencyScore;
}

