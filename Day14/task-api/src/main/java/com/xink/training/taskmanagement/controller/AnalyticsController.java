package com.xink.training.taskmanagement.controller;

import com.xink.training.taskmanagement.analytics.*;
import com.xink.training.taskmanagement.dto.AnalyticsRequest;
import com.xink.training.taskmanagement.service.TaskAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {
    
    private final TaskAnalyticsService analyticsService;
    
    @PostMapping("/task-analytics")
    public ResponseEntity<TaskAnalytics> getTaskAnalytics(@RequestBody AnalyticsRequest request) {
        return ResponseEntity.ok(analyticsService.generateTaskAnalytics(request));
    }
    
    @PostMapping("/productivity-report")
    public ResponseEntity<ProductivityReport> getProductivityReport(@RequestBody AnalyticsRequest request) {
        return ResponseEntity.ok(analyticsService.generateProductivityReport(request));
    }
    
    @PostMapping("/performance-metrics")
    public ResponseEntity<PerformanceMetrics> getPerformanceMetrics(@RequestBody AnalyticsRequest request) {
        List<com.xink.training.taskmanagement.domain.Task> tasks = 
            analyticsService.getTasksInDateRange(request.getFromDate(), request.getToDate());
        return ResponseEntity.ok(analyticsService.calculatePerformanceMetrics(tasks));
    }
    
    @PostMapping("/predictive-insights")
    public ResponseEntity<PredictiveInsights> getPredictiveInsights(@RequestBody AnalyticsRequest request) {
        List<com.xink.training.taskmanagement.domain.Task> historicalTasks = 
            analyticsService.getTasksInDateRange(request.getFromDate(), request.getToDate());
        List<com.xink.training.taskmanagement.domain.Task> currentTasks = 
            analyticsService.getCurrentTasks();
        return ResponseEntity.ok(analyticsService.generatePredictiveInsights(historicalTasks, currentTasks));
    }
}

