package com.xink.training.taskmanagement.analytics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PredictiveInsights {
    private List<TaskCompletionPrediction> completionPredictions;
    private List<TaskRisk> identifiedRisks;
    private List<String> recommendedActions;
    private double confidenceLevel;
}

