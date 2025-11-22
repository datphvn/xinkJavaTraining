package com.xink.training.taskmanagement.analytics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductivityReport {
    private Period reportPeriod;
    private Map<String, Object> overallMetrics;
    private Map<LocalDate, DailyProductivity> dailyProductivity;
    private List<WeeklyTrend> weeklyTrends;
    private Map<String, UserProductivity> userProductivity;
    private Map<String, ProjectMetrics> projectMetrics;
    private List<String> efficiencyInsights;
    private List<String> recommendations;
}

