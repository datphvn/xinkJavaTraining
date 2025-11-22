package com.xink.training.taskmanagement.analytics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.xink.training.taskmanagement.domain.User;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkloadImbalance {
    private User user;
    private long currentLoad;
    private double averageLoad;
    private double variance;
}

