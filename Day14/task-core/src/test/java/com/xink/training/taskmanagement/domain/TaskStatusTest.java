package com.xink.training.taskmanagement.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskStatusTest {
    
    @Test
    void testTodoStatus() {
        TaskStatus.Todo todo = new TaskStatus.Todo();
        assertEquals("To Do", todo.getDisplayName());
        assertTrue(todo.canTransitionTo(new TaskStatus.InProgress()));
        assertTrue(todo.canTransitionTo(new TaskStatus.Cancelled()));
        assertFalse(todo.canTransitionTo(new TaskStatus.Done()));
    }
    
    @Test
    void testInProgressStatus() {
        TaskStatus.InProgress inProgress = new TaskStatus.InProgress();
        assertEquals("In Progress", inProgress.getDisplayName());
        assertTrue(inProgress.canTransitionTo(new TaskStatus.Review("reviewer")));
        assertTrue(inProgress.canTransitionTo(new TaskStatus.Done()));
        assertFalse(inProgress.canTransitionTo(new TaskStatus.Todo()));
    }
    
    @Test
    void testDoneStatus() {
        TaskStatus.Done done = new TaskStatus.Done();
        assertEquals("Completed", done.getDisplayName());
        assertTrue(done.canTransitionTo(new TaskStatus.InProgress())); // Can reopen
        assertFalse(done.canTransitionTo(new TaskStatus.Todo()));
    }
    
    @Test
    void testCancelledStatus() {
        TaskStatus.Cancelled cancelled = new TaskStatus.Cancelled("Test reason");
        assertEquals("Cancelled", cancelled.getDisplayName());
        assertEquals("Test reason", cancelled.reason());
        assertTrue(cancelled.canTransitionTo(new TaskStatus.Todo()));
        assertTrue(cancelled.canTransitionTo(new TaskStatus.InProgress()));
    }
}

