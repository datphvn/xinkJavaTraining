package com.xink.training.taskmanagement.service;

import com.xink.training.taskmanagement.domain.*;
import com.xink.training.taskmanagement.dto.CreateTaskRequest;
import com.xink.training.taskmanagement.dto.TaskSearchCriteria;
import com.xink.training.taskmanagement.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {
    
    @Mock
    private TaskRepository taskRepository;
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private ProjectRepository projectRepository;
    
    @Mock
    private NotificationService notificationService;
    
    @Mock
    private TaskAnalyticsService analyticsService;
    
    @InjectMocks
    private TaskService taskService;
    
    private User testUser;
    private Project testProject;
    
    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(UUID.randomUUID());
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setDisplayName("Test User");
        testUser.setActive(true);
        
        testProject = new Project();
        testProject.setId(UUID.randomUUID());
        testProject.setName("Test Project");
    }
    
    @Test
    void testCreateTask_Success() {
        CreateTaskRequest request = CreateTaskRequest.builder()
            .title("Test Task")
            .description("Test Description")
            .priority("HIGH")
            .creatorId(testUser.getId())
            .build();
        
        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        Task task = taskService.createTask(request);
        
        assertNotNull(task);
        assertEquals("Test Task", task.getTitle());
        assertEquals("Test Description", task.getDescription());
        assertEquals(TaskPriority.HIGH, task.getPriority());
        verify(taskRepository, times(1)).save(any(Task.class));
        verify(notificationService, times(1)).notifyTaskCreated(any(Task.class));
    }
    
    @Test
    void testCreateTask_WithAssignee() {
        CreateTaskRequest request = CreateTaskRequest.builder()
            .title("Test Task")
            .creatorId(testUser.getId())
            .assigneeId(testUser.getId())
            .build();
        
        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        Task task = taskService.createTask(request);
        
        assertNotNull(task);
        assertEquals(testUser, task.getAssignee());
    }
    
    @Test
    void testSearchTasks_ByText() {
        Task task1 = createTestTask("Task One", "Description One");
        Task task2 = createTestTask("Task Two", "Description Two");
        List<Task> allTasks = Arrays.asList(task1, task2);
        
        when(taskRepository.findAll()).thenReturn(allTasks);
        
        TaskSearchCriteria criteria = TaskSearchCriteria.builder()
            .searchText("One")
            .build();
        
        List<Task> results = taskService.searchTasks(criteria);
        
        assertEquals(1, results.size());
        assertEquals("Task One", results.get(0).getTitle());
    }
    
    @Test
    void testSearchTasks_ByPriority() {
        Task task1 = createTestTask("Task One", "Description");
        task1.setPriority(TaskPriority.HIGH);
        Task task2 = createTestTask("Task Two", "Description");
        task2.setPriority(TaskPriority.LOW);
        List<Task> allTasks = Arrays.asList(task1, task2);
        
        when(taskRepository.findAll()).thenReturn(allTasks);
        
        TaskSearchCriteria criteria = TaskSearchCriteria.builder()
            .priorities(Arrays.asList("HIGH"))
            .build();
        
        List<Task> results = taskService.searchTasks(criteria);
        
        assertEquals(1, results.size());
        assertEquals(TaskPriority.HIGH, results.get(0).getPriority());
    }
    
    @Test
    void testBulkUpdateTasks() {
        UUID taskId1 = UUID.randomUUID();
        UUID taskId2 = UUID.randomUUID();
        
        Task task1 = createTestTask("Task 1", "Description");
        Task task2 = createTestTask("Task 2", "Description");
        
        when(taskRepository.findById(taskId1)).thenReturn(Optional.of(task1));
        when(taskRepository.findById(taskId2)).thenReturn(Optional.of(task2));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        com.xink.training.taskmanagement.dto.BulkUpdateRequest request = 
            com.xink.training.taskmanagement.dto.BulkUpdateRequest.builder()
                .taskIds(Arrays.asList(taskId1, taskId2))
                .newPriority("URGENT")
                .build();
        
        var future = taskService.bulkUpdateTasks(request);
        var result = future.join();
        
        assertEquals(2, result.getTotalRequested());
        assertTrue(result.getSuccessful() > 0);
    }
    
    private Task createTestTask(String title, String description) {
        Task task = new Task();
        task.setId(UUID.randomUUID());
        task.setTitle(title);
        task.setDescription(description);
        task.setPriority(TaskPriority.MEDIUM);
        task.setStatus(new TaskStatus.Todo());
        task.setCreator(testUser);
        task.setAuditInfo(new AuditInfo());
        return task;
    }
}

