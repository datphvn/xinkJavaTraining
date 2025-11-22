package com.xink.training.taskmanagement.analytics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyProductivity {
    private Duration totalWorkTime;
    private long tasksWorkedOn;
    private long activeUsers;
    private Map<String, Duration> timeByType;
    private Duration averageSessionLength;
}

