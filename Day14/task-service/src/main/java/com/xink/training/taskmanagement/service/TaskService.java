package com.xink.training.taskmanagement.service;

import com.xink.training.taskmanagement.domain.*;
import com.xink.training.taskmanagement.domain.exception.TaskCreationException;
import com.xink.training.taskmanagement.domain.exception.UserNotFoundException;
import com.xink.training.taskmanagement.dto.*;
import com.xink.training.taskmanagement.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class TaskService {
    
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final NotificationService notificationService;
    private final TaskAnalyticsService analyticsService;
    
    // Task Creation với Validation Pipeline
    public Task createTask(CreateTaskRequest request) {
        return validateTaskRequest(request)
            .map(this::buildTaskFromRequest)
            .map(this::applyBusinessRules)
            .map(taskRepository::save)
            .map(this::sendCreationNotifications)
            .orElseThrow(() -> new TaskCreationException("Failed to create task"));
    }
    
    private Optional<CreateTaskRequest> validateTaskRequest(CreateTaskRequest request) {
        return Optional.ofNullable(request)
            .filter(req -> req.getTitle() != null && !req.getTitle().trim().isEmpty())
            .filter(req -> req.getCreatorId() != null)
            .filter(this::validateProjectAccess)
            .filter(this::validateAssigneeAccess);
    }
    
    private boolean validateProjectAccess(CreateTaskRequest request) {
        if (request.getProjectId() == null) {
            return true;
        }
        return projectRepository.findById(request.getProjectId()).isPresent();
    }
    
    private boolean validateAssigneeAccess(CreateTaskRequest request) {
        if (request.getAssigneeId() == null) {
            return true;
        }
        return userRepository.findById(request.getAssigneeId()).isPresent();
    }
    
    private Task buildTaskFromRequest(CreateTaskRequest request) {
        User creator = userRepository.findById(request.getCreatorId())
            .orElseThrow(() -> new UserNotFoundException(request.getCreatorId()));
        
        Task.TaskBuilder builder = Task.builder()
            .title(request.getTitle())
            .description(request.getDescription())
            .priority(TaskPriority.fromString(request.getPriority()))
            .creator(creator);
        
        Optional.ofNullable(request.getAssigneeId())
            .flatMap(userRepository::findById)
            .ifPresent(builder::assignTo);
        
        Optional.ofNullable(request.getProjectId())
            .flatMap(projectRepository::findById)
            .ifPresent(builder::inProject);
        
        Optional.ofNullable(request.getDueDate())
            .ifPresent(builder::dueDate);
        
        Optional.ofNullable(request.getEstimatedHours())
            .map(hours -> Duration.ofHours(hours))
            .ifPresent(builder::estimatedDuration);
        
        Optional.ofNullable(request.getLabels())
            .ifPresent(labels -> labels.forEach(builder::withLabel));
        
        return builder.build();
    }
    
    private Task applyBusinessRules(Task task) {
        // Apply any business rules here
        return task;
    }
    
    private Task sendCreationNotifications(Task task) {
        notificationService.notifyTaskCreated(task);
        return task;
    }
    
    // Advanced Search với Stream Processing
    public List<Task> searchTasks(TaskSearchCriteria criteria) {
        return taskRepository.findAll().stream()
            .filter(createSearchPredicate(criteria))
            .sorted(createSortComparator(criteria.getSortBy(), criteria.getSortDirection()))
            .skip(criteria.getOffset())
            .limit(criteria.getLimit())
            .collect(Collectors.toList());
    }
    
    private Predicate<Task> createSearchPredicate(TaskSearchCriteria criteria) {
        return Stream.of(
                createTextSearchPredicate(criteria.getSearchText()),
                createStatusPredicate(criteria.getStatuses()),
                createPriorityPredicate(criteria.getPriorities()),
                createAssigneePredicate(criteria.getAssigneeIds()),
                createDateRangePredicate(criteria.getFromDate(), criteria.getToDate()),
                createLabelPredicate(criteria.getLabels()),
                createProjectPredicate(criteria.getProjectIds())
            )
            .filter(Objects::nonNull)
            .reduce(Predicate::and)
            .orElse(task -> true);
    }
    
    private Predicate<Task> createTextSearchPredicate(String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            return null;
        }
        
        String lowerSearch = searchText.toLowerCase();
        return task -> 
            task.getTitle().toLowerCase().contains(lowerSearch) ||
            Optional.ofNullable(task.getDescription())
                .map(desc -> desc.toLowerCase().contains(lowerSearch))
                .orElse(false);
    }
    
    private Predicate<Task> createStatusPredicate(List<String> statuses) {
        if (statuses == null || statuses.isEmpty()) {
            return null;
        }
        
        Set<Class<? extends TaskStatus>> statusTypes = statuses.stream()
            .map(this::parseStatusType)
            .collect(Collectors.toSet());
        
        return task -> statusTypes.contains(task.getStatus().getClass());
    }
    
    private Predicate<Task> createPriorityPredicate(List<String> priorities) {
        if (priorities == null || priorities.isEmpty()) {
            return null;
        }
        
        Set<TaskPriority> prioritySet = priorities.stream()
            .map(TaskPriority::fromString)
            .collect(Collectors.toSet());
        
        return task -> prioritySet.contains(task.getPriority());
    }
    
    private Predicate<Task> createAssigneePredicate(List<UUID> assigneeIds) {
        if (assigneeIds == null || assigneeIds.isEmpty()) {
            return null;
        }
        
        return task -> task.getAssignee() != null && 
                      assigneeIds.contains(task.getAssignee().getId());
    }
    
    private Predicate<Task> createDateRangePredicate(LocalDateTime fromDate, LocalDateTime toDate) {
        if (fromDate == null && toDate == null) {
            return null;
        }
        
        return task -> {
            LocalDateTime taskDate = task.getDueDate();
            if (taskDate == null) return false;
            
            boolean afterFrom = fromDate == null || !taskDate.isBefore(fromDate);
            boolean beforeTo = toDate == null || !taskDate.isAfter(toDate);
            return afterFrom && beforeTo;
        };
    }
    
    private Predicate<Task> createLabelPredicate(List<String> labels) {
        if (labels == null || labels.isEmpty()) {
            return null;
        }
        
        return task -> labels.stream()
            .anyMatch(label -> task.getLabels().contains(label));
    }
    
    private Predicate<Task> createProjectPredicate(List<UUID> projectIds) {
        if (projectIds == null || projectIds.isEmpty()) {
            return null;
        }
        
        return task -> task.getProject() != null && 
                      projectIds.contains(task.getProject().getId());
    }
    
    private Comparator<Task> createSortComparator(String sortBy, String sortDirection) {
        Comparator<Task> comparator;
        
        if (sortBy == null || sortBy.isEmpty()) {
            comparator = Comparator.comparing(Task::getTitle);
        } else {
            switch (sortBy.toLowerCase()) {
                case "priority" -> comparator = Comparator.comparing(Task::getPriority, 
                    Comparator.comparing(TaskPriority::getLevel));
                case "dueDate" -> comparator = Comparator.comparing(Task::getDueDate, 
                    Comparator.nullsLast(Comparator.naturalOrder()));
                case "status" -> comparator = Comparator.comparing(task -> task.getStatus().getDisplayName());
                case "title" -> comparator = Comparator.comparing(Task::getTitle);
                default -> comparator = Comparator.comparing(Task::getTitle);
            }
        }
        
        if ("desc".equalsIgnoreCase(sortDirection)) {
            comparator = comparator.reversed();
        }
        
        return comparator;
    }
    
    private Class<? extends TaskStatus> parseStatusType(String status) {
        return switch (status.toLowerCase()) {
            case "todo" -> TaskStatus.Todo.class;
            case "inprogress" -> TaskStatus.InProgress.class;
            case "review" -> TaskStatus.Review.class;
            case "done" -> TaskStatus.Done.class;
            case "cancelled" -> TaskStatus.Cancelled.class;
            default -> TaskStatus.Todo.class;
        };
    }
    
    private TaskStatus parseStatus(String status) {
        return switch (status.toLowerCase()) {
            case "todo" -> new TaskStatus.Todo();
            case "inprogress" -> new TaskStatus.InProgress();
            case "review" -> new TaskStatus.Review("system");
            case "done" -> new TaskStatus.Done();
            case "cancelled" -> new TaskStatus.Cancelled("System");
            default -> new TaskStatus.Todo();
        };
    }
    
    private User getCurrentUser() {
        // In real implementation, get from security context
        return userRepository.findAll().stream().findFirst().orElse(null);
    }
    
    // Bulk Operations với Parallel Processing
    @Async
    public CompletableFuture<BulkUpdateResult> bulkUpdateTasks(BulkUpdateRequest request) {
        List<UUID> taskIds = request.getTaskIds();
        
        List<CompletableFuture<TaskUpdateResult>> futures = taskIds.parallelStream()
            .map(taskId -> CompletableFuture.supplyAsync(() -> 
                processSingleTaskUpdate(taskId, request)))
            .collect(Collectors.toList());
        
        CompletableFuture<List<TaskUpdateResult>> allFutures = CompletableFuture.allOf(
                futures.toArray(new CompletableFuture[0]))
            .thenApply(v -> futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList()));
        
        return allFutures.thenApply(results -> BulkUpdateResult.builder()
            .totalRequested(taskIds.size())
            .successful(results.stream().mapToInt(r -> r.isSuccess() ? 1 : 0).sum())
            .failed(results.stream().mapToInt(r -> r.isSuccess() ? 0 : 1).sum())
            .results(results)
            .build());
    }
    
    private TaskUpdateResult processSingleTaskUpdate(UUID taskId, BulkUpdateRequest request) {
        try {
            return taskRepository.findById(taskId)
                .map(task -> applyBulkUpdates(task, request))
                .map(taskRepository::save)
                .map(task -> TaskUpdateResult.success(taskId))
                .orElse(TaskUpdateResult.failure(taskId, "Task not found"));
        } catch (Exception e) {
            log.error("Failed to update task {}: {}", taskId, e.getMessage(), e);
            return TaskUpdateResult.failure(taskId, e.getMessage());
        }
    }
    
    private Task applyBulkUpdates(Task task, BulkUpdateRequest request) {
        Optional.ofNullable(request.getNewPriority())
            .map(TaskPriority::fromString)
            .ifPresent(priority -> task.updatePriority(priority, "Bulk update"));
        
        Optional.ofNullable(request.getNewAssigneeId())
            .flatMap(userRepository::findById)
            .ifPresent(task::assignTo);
        
        Optional.ofNullable(request.getNewStatus())
            .map(this::parseStatus)
            .ifPresent(status -> task.updateStatus(status, getCurrentUser(), "Bulk update"));
        
        Optional.ofNullable(request.getLabelsToAdd())
            .ifPresent(labels -> labels.forEach(task::addLabel));
        
        Optional.ofNullable(request.getLabelsToRemove())
            .ifPresent(labels -> labels.forEach(task::removeLabel));
        
        return task;
    }
    
    public Optional<Task> findById(UUID id) {
        return taskRepository.findById(id);
    }
    
    public List<Task> findAll() {
        return taskRepository.findAll();
    }
    
    public Task updateTask(UUID id, CreateTaskRequest request) {
        return taskRepository.findById(id)
            .map(task -> {
                task.setTitle(request.getTitle());
                task.setDescription(request.getDescription());
                if (request.getPriority() != null) {
                    task.setPriority(TaskPriority.fromString(request.getPriority()));
                }
                Optional.ofNullable(request.getAssigneeId())
                    .flatMap(userRepository::findById)
                    .ifPresent(task::assignTo);
                return taskRepository.save(task);
            })
            .orElseThrow(() -> new RuntimeException("Task not found"));
    }
    
    public void deleteTask(UUID id) {
        taskRepository.deleteById(id);
    }
}

