package com.xink.training.taskmanagement.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    
    private User testUser;
    private Task task;
    
    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(java.util.UUID.randomUUID());
        testUser.setUsername("testuser");
        testUser.setActive(true);
        
        task = Task.builder()
            .title("Test Task")
            .description("Test Description")
            .priority(TaskPriority.MEDIUM)
            .creator(testUser)
            .build();
    }
    
    @Test
    void testTaskCreation() {
        assertNotNull(task);
        assertEquals("Test Task", task.getTitle());
        assertEquals("Test Description", task.getDescription());
        assertEquals(TaskPriority.MEDIUM, task.getPriority());
        assertNotNull(task.getStatus());
        assertTrue(task.getStatus() instanceof TaskStatus.Todo);
    }
    
    @Test
    void testAssignTo() {
        task.assignTo(testUser);
        assertEquals(testUser, task.getAssignee());
        assertFalse(task.getComments().isEmpty());
    }
    
    @Test
    void testUpdateStatus_ValidTransition() {
        TaskStatus newStatus = new TaskStatus.InProgress();
        task.updateStatus(newStatus, testUser, "Starting work");
        
        assertTrue(task.getStatus() instanceof TaskStatus.InProgress);
    }
    
    @Test
    void testUpdateStatus_InvalidTransition() {
        TaskStatus doneStatus = new TaskStatus.Done();
        
        assertThrows(InvalidStatusTransitionException.class, () -> {
            task.updateStatus(doneStatus, testUser, "Invalid");
        });
    }
    
    @Test
    void testIsOverdue_NotOverdue() {
        task.setDueDate(LocalDateTime.now().plusDays(1));
        assertFalse(task.isOverdue());
    }
    
    @Test
    void testIsOverdue_Overdue() {
        task.setDueDate(LocalDateTime.now().minusDays(1));
        assertTrue(task.isOverdue());
    }
    
    @Test
    void testIsOverdue_Completed() {
        task.setDueDate(LocalDateTime.now().minusDays(1));
        task.setStatus(new TaskStatus.Done());
        assertFalse(task.isOverdue());
    }
    
    @Test
    void testAddTimeEntry() {
        Duration duration = Duration.ofHours(2);
        task.addTimeEntry(duration, "Work session", testUser);
        
        assertEquals(1, task.getTimeEntries().size());
        assertEquals(duration, task.getActualDuration());
    }
    
    @Test
    void testGetCompletionPercentage_NoSubtasks() {
        task.setStatus(new TaskStatus.InProgress());
        assertEquals(50.0, task.getCompletionPercentage());
        
        task.setStatus(new TaskStatus.Done());
        assertEquals(100.0, task.getCompletionPercentage());
    }
    
    @ParameterizedTest
    @MethodSource("statusTransitionProvider")
    void testStatusTransitions(TaskStatus from, TaskStatus to, boolean shouldSucceed) {
        task.setStatus(from);
        
        if (shouldSucceed) {
            assertDoesNotThrow(() -> task.updateStatus(to, testUser, "Test"));
        } else {
            assertThrows(InvalidStatusTransitionException.class, 
                () -> task.updateStatus(to, testUser, "Test"));
        }
    }
    
    static Stream<Arguments> statusTransitionProvider() {
        return Stream.of(
            Arguments.of(new TaskStatus.Todo(), new TaskStatus.InProgress(), true),
            Arguments.of(new TaskStatus.Todo(), new TaskStatus.Done(), false),
            Arguments.of(new TaskStatus.InProgress(), new TaskStatus.Done(), true),
            Arguments.of(new TaskStatus.InProgress(), new TaskStatus.Todo(), false)
        );
    }
}

