package com.xink.training.day13.exercise5;

import com.xink.training.day13.model.User;
import com.xink.training.day13.service.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@DisplayName("Advanced Mocking Patterns")
@ExtendWith(MockitoExtension.class)
class AdvancedMockingTest {
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private EmailService emailService;
    
    @Mock
    private AuditService auditService;
    
    @Captor
    private ArgumentCaptor<User> userCaptor;
    
    @Captor
    private ArgumentCaptor<AuditEvent> auditEventCaptor;
    
    @Test
    @DisplayName("Should mock method calls with different return values")
    void shouldMockMethodCallsWithDifferentReturnValues() {
        // Setup different responses for same method
        when(userRepository.findByEmail("user1@example.com"))
            .thenReturn(Optional.of(new User("User 1", "user1@example.com")));
        when(userRepository.findByEmail("user2@example.com"))
            .thenReturn(Optional.of(new User("User 2", "user2@example.com")));
        when(userRepository.findByEmail("notfound@example.com"))
            .thenReturn(Optional.empty());
        
        // Test different scenarios
        Optional<User> user1 = userRepository.findByEmail("user1@example.com");
        Optional<User> user2 = userRepository.findByEmail("user2@example.com");
        Optional<User> user3 = userRepository.findByEmail("notfound@example.com");
        
        assertThat(user1).isPresent().get().extracting(User::getName).isEqualTo("User 1");
        assertThat(user2).isPresent().get().extracting(User::getName).isEqualTo("User 2");
        assertThat(user3).isEmpty();
    }
    
    @Test
    @DisplayName("Should handle method calls with complex argument matching")
    void shouldHandleComplexArgumentMatching() {
        // Mock với argument matchers
        when(userRepository.findByAgeRange(anyInt(), anyInt()))
            .thenReturn(Arrays.asList(
                new User("Young User", "young@example.com"),
                new User("Another Young User", "another@example.com")
            ));
        
        when(userRepository.findByNamePattern(argThat(pattern -> pattern.contains("*"))))
            .thenReturn(Arrays.asList(new User("Pattern Match", "pattern@example.com")));
        
        // Custom argument matcher
        when(userRepository.save(argThat(user -> 
            user.getEmail().endsWith("@company.com") && user.getName().length() > 5)))
            .thenAnswer(invocation -> {
                User user = invocation.getArgument(0);
                user.setId(999L);  // Simulate ID generation
                return user;
            });
        
        // Test complex matching
        List<User> youngUsers = userRepository.findByAgeRange(18, 25);
        List<User> patternUsers = userRepository.findByNamePattern("John*");
        
        User companyUser = new User("John Smith", "john.smith@company.com");
        User savedUser = userRepository.save(companyUser);
        
        assertThat(youngUsers).hasSize(2);
        assertThat(patternUsers).hasSize(1);
        assertThat(savedUser.getId()).isEqualTo(999L);
    }
    
    @Test
    @DisplayName("Should verify method calls with argument capture")
    void shouldVerifyMethodCallsWithArgumentCapture() {
        // Given
        UserService userService = new UserService(userRepository, emailService, auditService);
        User newUser = new User("John Doe", "john@example.com");
        when(userRepository.save(any(User.class)))
            .thenReturn(newUser);
        
        // When
        userService.createUserWithAudit(newUser);
        
        // Then - Capture và verify arguments
        verify(userRepository).save(userCaptor.capture());
        verify(auditService).logEvent(auditEventCaptor.capture());
        
        User capturedUser = userCaptor.getValue();
        AuditEvent capturedEvent = auditEventCaptor.getValue();
        
        assertThat(capturedUser)
            .extracting(User::getName, User::getEmail)
            .containsExactly("John Doe", "john@example.com");
        
        assertThat(capturedEvent)
            .extracting(AuditEvent::getAction, AuditEvent::getEntityType)
            .containsExactly("CREATE_USER", "User");
    }
    
    @Test
    @DisplayName("Should handle sequential method calls")
    void shouldHandleSequentialMethodCalls() {
        // Setup sequential return values
        when(userRepository.count())
            .thenReturn(100L)
            .thenReturn(101L)
            .thenReturn(102L);
        
        // Test multiple calls
        assertEquals(100L, userRepository.count());
        assertEquals(101L, userRepository.count());
        assertEquals(102L, userRepository.count());
        assertEquals(102L, userRepository.count()); // Last value repeats
    }
    
    @Test
    @DisplayName("Should handle exceptions in mocked methods")
    void shouldHandleExceptionsInMockedMethods() {
        // Mock method to throw exception
        when(userRepository.findById(999L))
            .thenThrow(new DatabaseConnectionException("Database unavailable"));
        
        when(emailService.sendEmail(anyString(), anyString()))
            .thenThrow(new EmailServiceException("SMTP server down"))
            .thenReturn(true); // Second call succeeds
        
        // Test exception handling
        assertThatThrownBy(() -> userRepository.findById(999L))
            .isInstanceOf(DatabaseConnectionException.class)
            .hasMessage("Database unavailable");
        
        // Test retry logic
        assertThrows(EmailServiceException.class, () -> emailService.sendEmail("test@example.com", "Subject"));
        assertTrue(emailService.sendEmail("test@example.com", "Subject"));
        
        verify(emailService, times(2)).sendEmail(anyString(), anyString());
    }
    
    @Test
    @DisplayName("Should handle void methods with doNothing/doThrow")
    void shouldHandleVoidMethods() {
        // Setup void method behaviors
        doNothing().when(auditService).logEvent(any(AuditEvent.class));
        doThrow(new AuditException("Audit system down"))
            .when(auditService).logCriticalEvent(any(AuditEvent.class));
        
        // Test void method calls
        assertDoesNotThrow(() -> 
            auditService.logEvent(new AuditEvent("TEST", "Test event")));
        
        assertThatThrownBy(() -> 
            auditService.logCriticalEvent(new AuditEvent("CRITICAL", "Critical event")))
            .isInstanceOf(AuditException.class);
        
        verify(auditService).logEvent(any(AuditEvent.class));
        verify(auditService).logCriticalEvent(any(AuditEvent.class));
    }
}

