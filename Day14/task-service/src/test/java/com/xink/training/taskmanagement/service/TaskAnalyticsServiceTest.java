package com.xink.training.taskmanagement.service;

import com.xink.training.taskmanagement.domain.*;
import com.xink.training.taskmanagement.dto.AnalyticsRequest;
import com.xink.training.taskmanagement.repository.TaskRepository;
import com.xink.training.taskmanagement.repository.TimeEntryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskAnalyticsServiceTest {
    
    @Mock
    private TaskRepository taskRepository;
    
    @Mock
    private TimeEntryRepository timeEntryRepository;
    
    @InjectMocks
    private TaskAnalyticsService analyticsService;
    
    private List<Task> testTasks;
    private User testUser;
    
    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(UUID.randomUUID());
        testUser.setUsername("testuser");
        testUser.setDisplayName("Test User");
        
        testTasks = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Task task = new Task();
            task.setId(UUID.randomUUID());
            task.setTitle("Task " + i);
            task.setPriority(TaskPriority.MEDIUM);
            task.setStatus(i < 3 ? new TaskStatus.Done() : new TaskStatus.Todo());
            task.setCreator(testUser);
            task.setAssignee(testUser);
            AuditInfo auditInfo = new AuditInfo();
            auditInfo.recordCreation("user1");
            task.setAuditInfo(auditInfo);
            testTasks.add(task);
        }
    }
    
    @Test
    void testGenerateTaskAnalytics() {
        when(taskRepository.findAll()).thenReturn(testTasks);
        
        AnalyticsRequest request = AnalyticsRequest.builder()
            .fromDate(LocalDate.now().minusDays(30))
            .toDate(LocalDate.now())
            .build();
        
        var analytics = analyticsService.generateTaskAnalytics(request);
        
        assertNotNull(analytics);
        assertEquals(5, analytics.getTotalTasks());
        assertEquals(3, analytics.getCompletedTasks());
        assertNotNull(analytics.getTasksByPriority());
    }
    
    @Test
    void testCalculatePerformanceMetrics() {
        var metrics = analyticsService.calculatePerformanceMetrics(testTasks);
        
        assertNotNull(metrics);
        assertEquals(5, metrics.getTotalTasks());
        assertTrue(metrics.getCompletionRate() > 0);
    }
    
    @Test
    void testAnalyzeWorkloadDistribution() {
        List<User> users = Arrays.asList(testUser);
        when(taskRepository.findAll()).thenReturn(testTasks);
        
        var analysis = analyticsService.analyzeWorkloadDistribution(testTasks, users);
        
        assertNotNull(analysis);
        assertNotNull(analysis.getUserWorkloads());
        assertTrue(analysis.getUserWorkloads().containsKey(testUser));
    }
}

