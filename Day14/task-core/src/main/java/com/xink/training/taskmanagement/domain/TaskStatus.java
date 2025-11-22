package com.xink.training.taskmanagement.domain;

import java.time.LocalDateTime;
import java.util.Optional;

public sealed interface TaskStatus permits Todo, InProgress, Review, Done, Cancelled {
    String getDisplayName();
    boolean canTransitionTo(TaskStatus newStatus);
    LocalDateTime getTimestamp();
    
    record Todo(LocalDateTime timestamp) implements TaskStatus {
        public Todo() { 
            this(LocalDateTime.now()); 
        }
        
        @Override
        public String getDisplayName() { 
            return "To Do"; 
        }
        
        @Override
        public boolean canTransitionTo(TaskStatus newStatus) {
            return newStatus instanceof InProgress || newStatus instanceof Cancelled;
        }
    }
    
    record InProgress(LocalDateTime timestamp, Optional<LocalDateTime> startedAt) implements TaskStatus {
        public InProgress() { 
            this(LocalDateTime.now(), Optional.of(LocalDateTime.now())); 
        }
        
        @Override
        public String getDisplayName() { 
            return "In Progress"; 
        }
        
        @Override
        public boolean canTransitionTo(TaskStatus newStatus) {
            return newStatus instanceof Review || newStatus instanceof Done || newStatus instanceof Cancelled;
        }
    }
    
    record Review(LocalDateTime timestamp, String reviewerId) implements TaskStatus {
        public Review(String reviewerId) {
            this(LocalDateTime.now(), reviewerId);
        }
        
        @Override
        public String getDisplayName() { 
            return "Under Review"; 
        }
        
        @Override
        public boolean canTransitionTo(TaskStatus newStatus) {
            return newStatus instanceof InProgress || newStatus instanceof Done || newStatus instanceof Cancelled;
        }
    }
    
    record Done(LocalDateTime timestamp, Optional<String> completedBy) implements TaskStatus {
        public Done() { 
            this(LocalDateTime.now(), Optional.empty()); 
        }
        
        public Done(String completedBy) {
            this(LocalDateTime.now(), Optional.of(completedBy));
        }
        
        @Override
        public String getDisplayName() { 
            return "Completed"; 
        }
        
        @Override
        public boolean canTransitionTo(TaskStatus newStatus) {
            return newStatus instanceof InProgress; // Can reopen
        }
    }
    
    record Cancelled(LocalDateTime timestamp, String reason) implements TaskStatus {
        public Cancelled(String reason) {
            this(LocalDateTime.now(), reason);
        }
        
        @Override
        public String getDisplayName() { 
            return "Cancelled"; 
        }
        
        @Override
        public boolean canTransitionTo(TaskStatus newStatus) {
            return newStatus instanceof Todo || newStatus instanceof InProgress;
        }
    }
}

