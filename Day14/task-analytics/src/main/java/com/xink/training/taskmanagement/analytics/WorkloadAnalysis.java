package com.xink.training.taskmanagement.analytics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.xink.training.taskmanagement.domain.User;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkloadAnalysis {
    private Map<User, WorkloadMetrics> userWorkloads;
    private List<WorkloadImbalance> imbalances;
    private double teamEfficiency;
    private List<String> recommendedRebalancing;
}

