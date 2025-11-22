package com.xink.training.taskmanagement.domain;

import java.util.Arrays;

public enum TaskPriority {
    LOW(1), MEDIUM(2), HIGH(3), URGENT(4), CRITICAL(5);
    
    private final int level;
    
    TaskPriority(int level) {
        this.level = level;
    }
    
    public int getLevel() {
        return level;
    }
    
    public boolean isHigherThan(TaskPriority other) {
        return this.level > other.level;
    }
    
    public static TaskPriority fromString(String priority) {
        return Arrays.stream(values())
            .filter(p -> p.name().equalsIgnoreCase(priority))
            .findFirst()
            .orElse(MEDIUM);
    }
}

