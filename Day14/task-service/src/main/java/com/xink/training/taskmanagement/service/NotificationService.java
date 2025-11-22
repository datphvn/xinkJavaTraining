package com.xink.training.taskmanagement.service;

import com.xink.training.taskmanagement.domain.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationService {
    
    public void notifyTaskCreated(Task task) {
        log.info("Task created: {} by {}", task.getTitle(), task.getCreator().getDisplayName());
        // In production, send actual notifications via email, SMS, etc.
    }
    
    public void notifyTaskUpdated(Task task) {
        log.info("Task updated: {}", task.getTitle());
    }
    
    public void notifyTaskAssigned(Task task) {
        if (task.getAssignee() != null) {
            log.info("Task {} assigned to {}", task.getTitle(), task.getAssignee().getDisplayName());
        }
    }
    
    public void notifyTaskCompleted(Task task) {
        log.info("Task completed: {}", task.getTitle());
    }
}

