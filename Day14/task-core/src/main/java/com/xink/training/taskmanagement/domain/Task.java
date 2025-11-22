package com.xink.training.taskmanagement.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "tasks", indexes = {
    @Index(name = "idx_task_status", columnList = "status"),
    @Index(name = "idx_task_priority", columnList = "priority"),
    @Index(name = "idx_task_due_date", columnList = "due_date"),
    @Index(name = "idx_task_assignee", columnList = "assignee_id")
})
@Getter
@Setter
@NoArgsConstructor
public class Task {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(nullable = false, length = 200)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskPriority priority = TaskPriority.MEDIUM;
    
    @Convert(converter = TaskStatusConverter.class)
    @Column(nullable = false)
    private TaskStatus status = new TaskStatus.Todo();
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignee_id")
    private User assignee;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;
    
    @Column(name = "due_date")
    private LocalDateTime dueDate;
    
    @Column(name = "estimated_hours")
    private Duration estimatedDuration;
    
    @Column(name = "actual_hours")
    private Duration actualDuration = Duration.ZERO;
    
    @ElementCollection
    @CollectionTable(name = "task_labels", joinColumns = @JoinColumn(name = "task_id"))
    @Column(name = "label")
    private Set<String> labels = new HashSet<>();
    
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TaskComment> comments = new ArrayList<>();
    
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private List<TimeEntry> timeEntries = new ArrayList<>();
    
    @ManyToMany
    @JoinTable(
        name = "task_dependencies",
        joinColumns = @JoinColumn(name = "task_id"),
        inverseJoinColumns = @JoinColumn(name = "depends_on_id")
    )
    private Set<Task> dependencies = new HashSet<>();
    
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Task> subtasks = new ArrayList<>();
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Task parent;
    
    @Embedded
    private AuditInfo auditInfo = new AuditInfo();
    
    // Business Logic Methods
    public void assignTo(User user) {
        validateCanAssign(user);
        this.assignee = user;
        addComment(TaskComment.systemComment(
            String.format("Task assigned to %s", user.getDisplayName())));
        auditInfo.recordUpdate();
    }
    
    public void updateStatus(TaskStatus newStatus, User updatedBy, String comment) {
        validateStatusTransition(newStatus);
        
        TaskStatus oldStatus = this.status;
        this.status = newStatus;
        
        addComment(TaskComment.statusChangeComment(
            String.format("Status changed from %s to %s", 
                oldStatus.getDisplayName(), newStatus.getDisplayName()),
            updatedBy, comment));
        
        handleStatusChange(oldStatus, newStatus);
        auditInfo.recordUpdate();
    }
    
    private void handleStatusChange(TaskStatus oldStatus, TaskStatus newStatus) {
        if (newStatus instanceof TaskStatus.InProgress) {
            if (!(oldStatus instanceof TaskStatus.InProgress)) {
                startTimeTracking();
            }
        } else if (newStatus instanceof TaskStatus.Done) {
            completeTask();
        } else if (newStatus instanceof TaskStatus.Cancelled) {
            stopTimeTracking();
        }
    }
    
    public void addTimeEntry(Duration duration, String description, User user) {
        TimeEntry entry = TimeEntry.builder()
            .task(this)
            .user(user)
            .duration(duration)
            .description(description)
            .date(java.time.LocalDate.now())
            .build();
        
        timeEntries.add(entry);
        actualDuration = actualDuration.plus(duration);
        auditInfo.recordUpdate();
    }
    
    public void updatePriority(TaskPriority newPriority, String reason) {
        TaskPriority oldPriority = this.priority;
        this.priority = newPriority;
        
        addComment(TaskComment.priorityChangeComment(
            String.format("Priority changed from %s to %s: %s", 
                oldPriority, newPriority, reason)));
    }
    
    public boolean isOverdue() {
        return dueDate != null && 
               LocalDateTime.now().isAfter(dueDate) && 
               !(status instanceof TaskStatus.Done);
    }
    
    public boolean isBlocked() {
        return dependencies.stream()
            .anyMatch(dep -> !(dep.getStatus() instanceof TaskStatus.Done));
    }
    
    public List<Task> getBlockedTasks() {
        return dependencies.stream()
            .filter(dep -> !(dep.getStatus() instanceof TaskStatus.Done))
            .collect(Collectors.toList());
    }
    
    public double getCompletionPercentage() {
        if (subtasks.isEmpty()) {
            return switch (status) {
                case TaskStatus.Done done -> 100.0;
                case TaskStatus.InProgress inProgress -> 50.0;
                case TaskStatus.Review review -> 80.0;
                default -> 0.0;
            };
        }
        
        return subtasks.stream()
            .mapToDouble(Task::getCompletionPercentage)
            .average()
            .orElse(0.0);
    }
    
    public Optional<LocalDateTime> getStartDate() {
        return timeEntries.stream()
            .map(TimeEntry::getDate)
            .map(date -> date.atStartOfDay())
            .min(LocalDateTime::compareTo);
    }
    
    public Optional<Duration> getAverageDailyWork() {
        Map<java.time.LocalDate, Duration> dailyWork = timeEntries.stream()
            .collect(Collectors.groupingBy(
                TimeEntry::getDate,
                Collectors.reducing(Duration.ZERO, TimeEntry::getDuration, Duration::plus)
            ));
        
        return dailyWork.isEmpty() ? Optional.empty() :
            Optional.of(dailyWork.values().stream()
                .reduce(Duration.ZERO, Duration::plus)
                .dividedBy(dailyWork.size()));
    }
    
    public Map<User, Duration> getTimeByUser() {
        return timeEntries.stream()
            .collect(Collectors.groupingBy(
                TimeEntry::getUser,
                Collectors.reducing(Duration.ZERO, TimeEntry::getDuration, Duration::plus)
            ));
    }
    
    private void validateCanAssign(User user) {
        if (user == null) {
            throw new TaskValidationException("Cannot assign task to null user");
        }
        
        if (!user.isActive()) {
            throw new TaskValidationException("Cannot assign task to inactive user");
        }
        
        if (project != null && !project.hasMember(user)) {
            throw new TaskValidationException("User is not a member of the project");
        }
    }
    
    private void validateStatusTransition(TaskStatus newStatus) {
        if (!status.canTransitionTo(newStatus)) {
            throw new InvalidStatusTransitionException(
                String.format("Cannot transition from %s to %s", 
                    status.getDisplayName(), newStatus.getDisplayName()));
        }
    }
    
    private void startTimeTracking() {
        // Implementation for starting time tracking
    }
    
    private void completeTask() {
        // Implementation for completing task
    }
    
    private void stopTimeTracking() {
        // Implementation for stopping time tracking
    }
    
    public void addComment(TaskComment comment) {
        comment.setTask(this);
        comments.add(comment);
    }
    
    public void addLabel(String label) {
        labels.add(label);
    }
    
    public void removeLabel(String label) {
        labels.remove(label);
    }
    
    // Builder Pattern
    public static class TaskBuilder {
        private String title;
        private String description;
        private TaskPriority priority = TaskPriority.MEDIUM;
        private User creator;
        private User assignee;
        private Project project;
        private LocalDateTime dueDate;
        private Duration estimatedDuration;
        private Set<String> labels = new HashSet<>();
        
        public TaskBuilder title(String title) {
            this.title = title;
            return this;
        }
        
        public TaskBuilder description(String description) {
            this.description = description;
            return this;
        }
        
        public TaskBuilder priority(TaskPriority priority) {
            this.priority = priority;
            return this;
        }
        
        public TaskBuilder creator(User creator) {
            this.creator = creator;
            return this;
        }
        
        public TaskBuilder assignTo(User assignee) {
            this.assignee = assignee;
            return this;
        }
        
        public TaskBuilder inProject(Project project) {
            this.project = project;
            return this;
        }
        
        public TaskBuilder dueDate(LocalDateTime dueDate) {
            this.dueDate = dueDate;
            return this;
        }
        
        public TaskBuilder estimatedDuration(Duration duration) {
            this.estimatedDuration = duration;
            return this;
        }
        
        public TaskBuilder withLabel(String label) {
            this.labels.add(label);
            return this;
        }
        
        public TaskBuilder withLabels(String... labels) {
            this.labels.addAll(Arrays.asList(labels));
            return this;
        }
        
        public Task build() {
            validateRequired();
            
            Task task = new Task();
            task.title = this.title;
            task.description = this.description;
            task.priority = this.priority;
            task.creator = this.creator;
            task.assignee = this.assignee;
            task.project = this.project;
            task.dueDate = this.dueDate;
            task.estimatedDuration = this.estimatedDuration;
            task.labels = new HashSet<>(this.labels);
            
            return task;
        }
        
        private void validateRequired() {
            if (title == null || title.trim().isEmpty()) {
                throw new IllegalArgumentException("Task title is required");
            }
            if (creator == null) {
                throw new IllegalArgumentException("Task creator is required");
            }
        }
    }
    
    public static TaskBuilder builder() {
        return new TaskBuilder();
    }
}

