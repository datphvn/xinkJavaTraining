package com.xink.training.taskmanagement.service;

import com.xink.training.taskmanagement.domain.*;
import com.xink.training.taskmanagement.analytics.*;
import com.xink.training.taskmanagement.dto.AnalyticsRequest;
import com.xink.training.taskmanagement.repository.TaskRepository;
import com.xink.training.taskmanagement.repository.TimeEntryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskAnalyticsService {
    
    private final TaskRepository taskRepository;
    private final TimeEntryRepository timeEntryRepository;
    
    // Productivity Analysis với Advanced Grouping
    public ProductivityReport generateProductivityReport(AnalyticsRequest request) {
        LocalDate fromDate = request.getFromDate();
        LocalDate toDate = request.getToDate();
        
        List<Task> tasks = getTasksInDateRange(fromDate, toDate);
        List<TimeEntry> timeEntries = getTimeEntriesInDateRange(fromDate, toDate);
        
        return ProductivityReport.builder()
            .reportPeriod(java.time.Period.between(fromDate, toDate))
            .overallMetrics(calculateOverallMetrics(tasks, timeEntries))
            .dailyProductivity(calculateDailyProductivity(timeEntries))
            .weeklyTrends(calculateWeeklyTrends(tasks, timeEntries))
            .userProductivity(calculateUserProductivity(tasks, timeEntries))
            .projectMetrics(calculateProjectMetrics(tasks))
            .efficiencyInsights(generateEfficiencyInsights(tasks, timeEntries))
            .recommendations(generateRecommendations(tasks, timeEntries))
            .build();
    }
    
    private Map<LocalDate, DailyProductivity> calculateDailyProductivity(List<TimeEntry> timeEntries) {
        return timeEntries.stream()
            .collect(Collectors.groupingBy(TimeEntry::getDate))
            .entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> calculateDailyMetrics(entry.getValue())
            ));
    }
    
    private DailyProductivity calculateDailyMetrics(List<TimeEntry> dailyEntries) {
        Duration totalTime = dailyEntries.stream()
            .map(TimeEntry::getDuration)
            .reduce(Duration.ZERO, Duration::plus);
        
        long uniqueTasks = dailyEntries.stream()
            .map(TimeEntry::getTask)
            .map(Task::getId)
            .distinct()
            .count();
        
        long uniqueUsers = dailyEntries.stream()
            .map(TimeEntry::getUser)
            .map(User::getId)
            .distinct()
            .count();
        
        Map<TimeEntryType, Duration> timeByType = dailyEntries.stream()
            .collect(Collectors.groupingBy(
                TimeEntry::getType,
                Collectors.reducing(Duration.ZERO, TimeEntry::getDuration, Duration::plus)
            ));
        
        OptionalDouble avgSessionLength = dailyEntries.stream()
            .mapToLong(entry -> entry.getDuration().toMinutes())
            .average();
        
        return DailyProductivity.builder()
            .totalWorkTime(totalTime)
            .tasksWorkedOn(uniqueTasks)
            .activeUsers(uniqueUsers)
            .timeByType(timeByType.entrySet().stream()
                .collect(Collectors.toMap(
                    e -> e.getKey().name(),
                    Map.Entry::getValue
                )))
            .averageSessionLength(avgSessionLength.isPresent() 
                ? Duration.ofMinutes((long) avgSessionLength.getAsDouble())
                : Duration.ZERO)
            .build();
    }
    
    // Advanced Performance Metrics với Statistical Analysis
    public PerformanceMetrics calculatePerformanceMetrics(List<Task> tasks) {
        double completionRate = tasks.stream()
            .collect(Collectors.partitioningBy(task -> task.getStatus() instanceof TaskStatus.Done))
            .entrySet().stream()
            .filter(Map.Entry::getKey)
            .findFirst()
            .map(entry -> (double) entry.getValue().size() / tasks.size() * 100)
            .orElse(0.0);
        
        Map<LocalDate, Long> completionsByDate = tasks.stream()
            .filter(task -> task.getStatus() instanceof TaskStatus.Done)
            .collect(Collectors.groupingBy(
                task -> task.getStatus().getTimestamp().toLocalDate(),
                Collectors.counting()
            ));
        
        OptionalDouble averageVelocity = completionsByDate.values().stream()
            .mapToLong(Long::longValue)
            .average();
        
        OptionalDouble averageLeadTime = tasks.stream()
            .filter(task -> task.getStatus() instanceof TaskStatus.Done)
            .filter(task -> task.getAuditInfo().getCreatedAt() != null)
            .mapToLong(task -> Duration.between(
                    task.getAuditInfo().getCreatedAt(),
                    task.getStatus().getTimestamp()
                ).toDays())
            .average();
        
        OptionalDouble averageCycleTime = tasks.stream()
            .filter(task -> task.getStatus() instanceof TaskStatus.Done)
            .map(Task::getStartDate)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .mapToLong(startDate -> Duration.between(
                    startDate,
                    LocalDateTime.now()
                ).toDays())
            .average();
        
        long reopenedTasks = tasks.stream()
            .mapToLong(task -> task.getComments().stream()
                .filter(comment -> comment.getType() == CommentType.STATUS_CHANGE)
                .filter(comment -> comment.getContent().contains("reopened"))
                .count())
            .sum();
        
        double defectRate = tasks.isEmpty() ? 0.0 : (double) reopenedTasks / tasks.size() * 100;
        
        return PerformanceMetrics.builder()
            .completionRate(completionRate)
            .averageVelocity(averageVelocity.orElse(0.0))
            .averageLeadTime(averageLeadTime.orElse(0.0))
            .averageCycleTime(averageCycleTime.orElse(0.0))
            .defectRate(defectRate)
            .totalTasks(tasks.size())
            .build();
    }
    
    // Workload Distribution Analysis
    public WorkloadAnalysis analyzeWorkloadDistribution(List<Task> tasks, List<User> users) {
        Map<User, WorkloadMetrics> userWorkloads = users.stream()
            .collect(Collectors.toMap(
                user -> user,
                user -> calculateUserWorkload(user, tasks)
            ));
        
        OptionalDouble averageTaskCount = userWorkloads.values().stream()
            .mapToLong(WorkloadMetrics::getAssignedTasks)
            .average();
        
        List<WorkloadImbalance> imbalances = Collections.emptyList();
        if (averageTaskCount.isPresent()) {
            double avgTasks = averageTaskCount.getAsDouble();
            double threshold = avgTasks * 0.3;
            
            imbalances = userWorkloads.entrySet().stream()
                .filter(entry -> Math.abs(entry.getValue().getAssignedTasks() - avgTasks) > threshold)
                .map(entry -> WorkloadImbalance.builder()
                    .user(entry.getKey())
                    .currentLoad(entry.getValue().getAssignedTasks())
                    .averageLoad(avgTasks)
                    .variance((entry.getValue().getAssignedTasks() - avgTasks) / avgTasks * 100)
                    .build())
                .sorted(Comparator.comparing(WorkloadImbalance::getVariance).reversed())
                .collect(Collectors.toList());
        }
        
        double teamEfficiency = calculateTeamEfficiency(tasks, userWorkloads.values());
        
        return WorkloadAnalysis.builder()
            .userWorkloads(userWorkloads)
            .imbalances(imbalances)
            .teamEfficiency(teamEfficiency)
            .recommendedRebalancing(generateRebalancingRecommendations(imbalances))
            .build();
    }
    
    private WorkloadMetrics calculateUserWorkload(User user, List<Task> tasks) {
        List<Task> userTasks = tasks.stream()
            .filter(task -> user.equals(task.getAssignee()))
            .collect(Collectors.toList());
        
        long assignedTasks = userTasks.size();
        long completedTasks = userTasks.stream()
            .filter(task -> task.getStatus() instanceof TaskStatus.Done)
            .count();
        
        double completionRate = assignedTasks == 0 ? 0.0 : 
            (double) completedTasks / assignedTasks * 100;
        
        Duration totalEstimatedWork = userTasks.stream()
            .map(Task::getEstimatedDuration)
            .filter(Objects::nonNull)
            .reduce(Duration.ZERO, Duration::plus);
        
        Duration totalActualWork = userTasks.stream()
            .map(Task::getActualDuration)
            .reduce(Duration.ZERO, Duration::plus);
        
        long overdueTasks = userTasks.stream()
            .filter(Task::isOverdue)
            .count();
        
        OptionalDouble averagePriority = userTasks.stream()
            .mapToInt(task -> task.getPriority().getLevel())
            .average();
        
        return WorkloadMetrics.builder()
            .assignedTasks(assignedTasks)
            .completedTasks(completedTasks)
            .completionRate(completionRate)
            .totalEstimatedWork(totalEstimatedWork)
            .totalActualWork(totalActualWork)
            .overdueTasks(overdueTasks)
            .averagePriorityLevel(averagePriority.orElse(0.0))
            .build();
    }
    
    private double calculateTeamEfficiency(List<Task> tasks, Collection<WorkloadMetrics> workloads) {
        if (workloads.isEmpty()) return 0.0;
        
        double avgCompletionRate = workloads.stream()
            .mapToDouble(WorkloadMetrics::getCompletionRate)
            .average()
            .orElse(0.0);
        
        return avgCompletionRate;
    }
    
    private List<String> generateRebalancingRecommendations(List<WorkloadImbalance> imbalances) {
        return imbalances.stream()
            .map(imbalance -> String.format("User %s has %.1f%% variance from average workload",
                imbalance.getUser().getDisplayName(), imbalance.getVariance()))
            .collect(Collectors.toList());
    }
    
    // Predictive Analytics using Historical Data
    public PredictiveInsights generatePredictiveInsights(List<Task> historicalTasks, List<Task> currentTasks) {
        Map<TaskPriority, OptionalDouble> avgCompletionTimeByPriority = historicalTasks.stream()
            .filter(task -> task.getStatus() instanceof TaskStatus.Done)
            .collect(Collectors.groupingBy(
                Task::getPriority,
                Collectors.averagingLong(task -> Duration.between(
                        task.getAuditInfo().getCreatedAt(),
                        task.getStatus().getTimestamp()
                    ).toDays())
            ))
            .entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> OptionalDouble.of(entry.getValue())
            ));
        
        List<TaskCompletionPrediction> predictions = currentTasks.stream()
            .filter(task -> !(task.getStatus() instanceof TaskStatus.Done))
            .map(task -> {
                OptionalDouble avgDays = avgCompletionTimeByPriority.getOrDefault(
                    task.getPriority(), OptionalDouble.empty());
                
                LocalDateTime predictedCompletion = avgDays.isPresent() ?
                    task.getAuditInfo().getCreatedAt().plusDays((long) avgDays.getAsDouble()) :
                    task.getDueDate();
                
                double confidence = calculatePredictionConfidence(task, historicalTasks);
                
                return TaskCompletionPrediction.builder()
                    .taskId(task.getId())
                    .taskTitle(task.getTitle())
                    .currentStatus(task.getStatus().getDisplayName())
                    .predictedCompletionDate(predictedCompletion)
                    .confidence(confidence)
                    .isLikelyToBeOverdue(predictedCompletion != null && 
                        task.getDueDate() != null &&
                        predictedCompletion.isAfter(task.getDueDate()))
                    .build();
            })
            .collect(Collectors.toList());
        
        List<TaskRisk> risks = identifyTaskRisks(currentTasks, historicalTasks);
        
        return PredictiveInsights.builder()
            .completionPredictions(predictions)
            .identifiedRisks(risks)
            .recommendedActions(generateActionRecommendations(predictions, risks))
            .confidenceLevel(calculateOverallConfidence(predictions))
            .build();
    }
    
    private double calculatePredictionConfidence(Task task, List<Task> historicalTasks) {
        long similarTasks = historicalTasks.stream()
            .filter(t -> t.getPriority() == task.getPriority())
            .count();
        
        return Math.min(100.0, similarTasks * 10.0);
    }
    
    private List<TaskRisk> identifyTaskRisks(List<Task> currentTasks, List<Task> historicalTasks) {
        return currentTasks.stream()
            .filter(Task::isOverdue)
            .map(task -> TaskRisk.builder()
                .taskId(task.getId())
                .taskTitle(task.getTitle())
                .riskType("OVERDUE")
                .description("Task is past due date")
                .riskLevel(0.8)
                .build())
            .collect(Collectors.toList());
    }
    
    private List<String> generateActionRecommendations(List<TaskCompletionPrediction> predictions, 
                                                       List<TaskRisk> risks) {
        List<String> recommendations = new ArrayList<>();
        
        predictions.stream()
            .filter(TaskCompletionPrediction::isLikelyToBeOverdue)
            .forEach(pred -> recommendations.add(
                String.format("Task '%s' is likely to be overdue", pred.getTaskTitle())));
        
        risks.forEach(risk -> recommendations.add(
            String.format("Address risk for task '%s': %s", risk.getTaskTitle(), risk.getDescription())));
        
        return recommendations;
    }
    
    private double calculateOverallConfidence(List<TaskCompletionPrediction> predictions) {
        return predictions.stream()
            .mapToDouble(TaskCompletionPrediction::getConfidence)
            .average()
            .orElse(0.0);
    }
    
    public TaskAnalytics generateTaskAnalytics(AnalyticsRequest request) {
        List<Task> tasks = getTasksInDateRange(request.getFromDate(), request.getToDate());
        
        return TaskAnalytics.builder()
            .totalTasks(tasks.size())
            .completedTasks(countTasksByStatus(tasks, TaskStatus.Done.class))
            .inProgressTasks(countTasksByStatus(tasks, TaskStatus.InProgress.class))
            .overdueTasks(countOverdueTasks(tasks))
            .averageCompletionTime(calculateAverageCompletionTime(tasks))
            .tasksByPriority(groupTasksByPriority(tasks))
            .tasksByAssignee(groupTasksByAssignee(tasks))
            .productivityTrends(calculateProductivityTrends(tasks))
            .topContributors(findTopContributors(tasks))
            .build();
    }
    
    private long countTasksByStatus(List<Task> tasks, Class<? extends TaskStatus> statusType) {
        return tasks.stream()
            .filter(task -> statusType.isInstance(task.getStatus()))
            .count();
    }
    
    private long countOverdueTasks(List<Task> tasks) {
        return tasks.stream()
            .filter(Task::isOverdue)
            .count();
    }
    
    private double calculateAverageCompletionTime(List<Task> tasks) {
        return tasks.stream()
            .filter(task -> task.getStatus() instanceof TaskStatus.Done)
            .filter(task -> task.getAuditInfo().getCreatedAt() != null)
            .mapToLong(task -> Duration.between(
                    task.getAuditInfo().getCreatedAt(),
                    task.getStatus().getTimestamp()
                ).toDays())
            .average()
            .orElse(0.0);
    }
    
    private Map<String, Long> groupTasksByPriority(List<Task> tasks) {
        return tasks.stream()
            .collect(Collectors.groupingBy(
                task -> task.getPriority().name(),
                Collectors.counting()
            ));
    }
    
    private Map<String, Long> groupTasksByAssignee(List<Task> tasks) {
        return tasks.stream()
            .filter(task -> task.getAssignee() != null)
            .collect(Collectors.groupingBy(
                task -> task.getAssignee().getDisplayName(),
                Collectors.counting()
            ));
    }
    
    private List<ProductivityTrend> calculateProductivityTrends(List<Task> tasks) {
        return tasks.stream()
            .filter(task -> task.getStatus() instanceof TaskStatus.Done)
            .collect(Collectors.groupingBy(
                task -> task.getStatus().getTimestamp().toLocalDate(),
                Collectors.counting()
            ))
            .entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .map(entry -> new ProductivityTrend(entry.getKey(), entry.getValue()))
            .collect(Collectors.toList());
    }
    
    private List<ContributorStats> findTopContributors(List<Task> tasks) {
        return tasks.stream()
            .flatMap(task -> task.getTimeEntries().stream())
            .collect(Collectors.groupingBy(
                TimeEntry::getUser,
                Collectors.reducing(Duration.ZERO, TimeEntry::getDuration, Duration::plus)
            ))
            .entrySet().stream()
            .sorted(Map.Entry.<User, Duration>comparingByValue().reversed())
            .limit(10)
            .map(entry -> new ContributorStats(
                entry.getKey().getDisplayName(),
                entry.getValue(),
                countCompletedTasksByUser(tasks, entry.getKey())
            ))
            .collect(Collectors.toList());
    }
    
    private long countCompletedTasksByUser(List<Task> tasks, User user) {
        return tasks.stream()
            .filter(task -> user.equals(task.getAssignee()))
            .filter(task -> task.getStatus() instanceof TaskStatus.Done)
            .count();
    }
    
    public List<Task> getTasksInDateRange(LocalDate fromDate, LocalDate toDate) {
        return taskRepository.findAll().stream()
            .filter(task -> {
                LocalDateTime taskDate = task.getAuditInfo().getCreatedAt();
                if (taskDate == null) return false;
                LocalDate taskLocalDate = taskDate.toLocalDate();
                return !taskLocalDate.isBefore(fromDate) && !taskLocalDate.isAfter(toDate);
            })
            .collect(Collectors.toList());
    }
    
    public List<TimeEntry> getTimeEntriesInDateRange(LocalDate fromDate, LocalDate toDate) {
        return timeEntryRepository.findTimeEntriesInDateRange(fromDate, toDate);
    }
    
    public List<Task> getCurrentTasks() {
        return taskRepository.findAll().stream()
            .filter(task -> !(task.getStatus() instanceof TaskStatus.Done))
            .collect(Collectors.toList());
    }
    
    private Map<String, Object> calculateOverallMetrics(List<Task> tasks, List<TimeEntry> timeEntries) {
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("totalTasks", tasks.size());
        metrics.put("totalTimeEntries", timeEntries.size());
        metrics.put("totalWorkTime", timeEntries.stream()
            .map(TimeEntry::getDuration)
            .reduce(Duration.ZERO, Duration::plus));
        return metrics;
    }
    
    private List<WeeklyTrend> calculateWeeklyTrends(List<Task> tasks, List<TimeEntry> timeEntries) {
        // Simplified implementation
        return Collections.emptyList();
    }
    
    private Map<String, UserProductivity> calculateUserProductivity(List<Task> tasks, List<TimeEntry> timeEntries) {
        return timeEntries.stream()
            .collect(Collectors.groupingBy(
                entry -> entry.getUser().getDisplayName(),
                Collectors.collectingAndThen(
                    Collectors.toList(),
                    entries -> {
                        long completed = tasks.stream()
                            .filter(t -> entries.stream().anyMatch(e -> e.getTask().equals(t)))
                            .filter(t -> t.getStatus() instanceof TaskStatus.Done)
                            .count();
                        Duration totalTime = entries.stream()
                            .map(TimeEntry::getDuration)
                            .reduce(Duration.ZERO, Duration::plus);
                        return new UserProductivity(completed, totalTime, 0.0, 0.0);
                    }
                )
            ));
    }
    
    private Map<String, ProjectMetrics> calculateProjectMetrics(List<Task> tasks) {
        return tasks.stream()
            .filter(task -> task.getProject() != null)
            .collect(Collectors.groupingBy(
                task -> task.getProject().getName(),
                Collectors.collectingAndThen(
                    Collectors.toList(),
                    projectTasks -> {
                        long total = projectTasks.size();
                        long completed = projectTasks.stream()
                            .filter(t -> t.getStatus() instanceof TaskStatus.Done)
                            .count();
                        return new ProjectMetrics(total, completed, 
                            total > 0 ? (double) completed / total * 100 : 0.0, 0.0);
                    }
                )
            ));
    }
    
    private List<String> generateEfficiencyInsights(List<Task> tasks, List<TimeEntry> timeEntries) {
        List<String> insights = new ArrayList<>();
        insights.add("Total tasks analyzed: " + tasks.size());
        insights.add("Total time entries: " + timeEntries.size());
        return insights;
    }
    
    private List<String> generateRecommendations(List<Task> tasks, List<TimeEntry> timeEntries) {
        List<String> recommendations = new ArrayList<>();
        long overdueCount = tasks.stream().filter(Task::isOverdue).count();
        if (overdueCount > 0) {
            recommendations.add("Address " + overdueCount + " overdue tasks");
        }
        return recommendations;
    }
}

