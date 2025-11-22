package com.xink.training.taskmanagement.analytics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContributorStats {
    private String userName;
    private Duration totalTime;
    private long completedTasks;
}

