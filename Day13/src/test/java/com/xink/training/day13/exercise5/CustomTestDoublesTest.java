package com.xink.training.day13.exercise5;

import com.xink.training.day13.model.User;
import com.xink.training.day13.service.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("Custom Test Doubles and Advanced Verification")
class CustomTestDoublesTest {
    
    @Test
    @DisplayName("Should use answer for complex method behavior")
    void shouldUseAnswerForComplexBehavior() {
        UserRepository mockRepository = mock(UserRepository.class);
        
        // Custom answer với complex logic
        when(mockRepository.save(any(User.class)))
            .thenAnswer(invocation -> {
                User user = invocation.getArgument(0);
                
                // Simulate ID generation based on email hash
                long generatedId = Math.abs(user.getEmail().hashCode()) % 10000;
                user.setId(generatedId);
                
                // Simulate timestamp setting
                user.setCreatedAt(java.time.LocalDateTime.now());
                
                // Simulate validation
                if (user.getName() == null || user.getName().trim().isEmpty()) {
                    throw new ValidationException("Name cannot be empty");
                }
                
                return user;
            });
        
        // Test the custom behavior
        User user = new User("John Doe", "john@example.com");
        User savedUser = mockRepository.save(user);
        
        assertThat(savedUser)
            .extracting(User::getId, User::getCreatedAt)
            .satisfies(id -> assertThat(id).isNotNull().isPositive(),
                      createdAt -> assertThat(createdAt).isNotNull());
        
        // Test validation logic
        User invalidUser = new User("", "invalid@example.com");
        assertThatThrownBy(() -> mockRepository.save(invalidUser))
            .isInstanceOf(ValidationException.class);
    }
    
    @Test
    @DisplayName("Should create fake implementations")
    void shouldCreateFakeImplementations() {
        // Create fake implementation instead of mock
        FakeUserRepository fakeRepository = new FakeUserRepository();
        UserService userService = new UserService(fakeRepository, null);
        
        // Test với fake implementation
        User user = new User("Jane Doe", "jane@example.com");
        User savedUser = userService.createUser(user);
        
        assertThat(savedUser.getId()).isNotNull();
        
        Optional<User> retrievedUser = userService.findUserByEmail("jane@example.com");
        assertThat(retrievedUser).isPresent();
    }
    
    @Test
    @DisplayName("Should verify interaction order")
    void shouldVerifyInteractionOrder() {
        UserRepository mockRepository = mock(UserRepository.class);
        EmailService mockEmailService = mock(EmailService.class);
        AuditService mockAuditService = mock(AuditService.class);
        
        UserService userService = new UserService(mockRepository, mockEmailService, mockAuditService);
        
        when(mockRepository.save(any(User.class)))
            .thenReturn(new User("John", "john@example.com"));
        when(mockEmailService.sendWelcomeEmail(anyString(), anyString()))
            .thenReturn(true);
        
        // Execute the operation
        userService.registerUser("John", "john@example.com");
        
        // Verify order of interactions
        InOrder inOrder = inOrder(mockRepository, mockEmailService, mockAuditService);
        
        inOrder.verify(mockRepository).save(any(User.class));
        inOrder.verify(mockEmailService).sendWelcomeEmail("john@example.com", "John");
    }
    
    @Test
    @DisplayName("Should verify no unwanted interactions")
    void shouldVerifyNoUnwantedInteractions() {
        UserRepository mockRepository = mock(UserRepository.class);
        EmailService mockEmailService = mock(EmailService.class);
        
        UserService userService = new UserService(mockRepository, mockEmailService);
        
        // Setup
        when(mockRepository.findByEmail("existing@example.com"))
            .thenReturn(Optional.of(new User("Existing", "existing@example.com")));
        
        // Execute operation that should not create new user
        Optional<User> user = userService.findUserByEmail("existing@example.com");
        
        // Verify expected interactions
        verify(mockRepository).findByEmail("existing@example.com");
        
        // Verify no unwanted interactions
        verify(mockRepository, never()).save(any(User.class));
        verify(mockEmailService, never()).sendWelcomeEmail(anyString(), anyString());
        
        // Verify no other interactions occurred
        verifyNoMoreInteractions(mockRepository);
        verifyNoInteractions(mockEmailService);
    }
    
    // Fake implementation for testing
    private static class FakeUserRepository implements UserRepository {
        private final Map<String, User> users = new HashMap<>();
        private long nextId = 1L;
        
        @Override
        public User save(User user) {
            if (user.getId() == null) {
                user.setId(nextId++);
            }
            users.put(user.getEmail(), user);
            return user;
        }
        
        @Override
        public Optional<User> findByEmail(String email) {
            return Optional.ofNullable(users.get(email));
        }
        
        @Override
        public Optional<User> findById(Long id) {
            return users.values().stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
        }
        
        @Override
        public boolean existsByEmail(String email) {
            return users.containsKey(email);
        }
        
        @Override
        public List<User> findByAgeRange(int minAge, int maxAge) {
            return users.values().stream()
                .filter(user -> user.getAge() >= minAge && user.getAge() <= maxAge)
                .toList();
        }
        
        @Override
        public List<User> findByNamePattern(String pattern) {
            String regex = pattern.replace("*", ".*");
            return users.values().stream()
                .filter(user -> user.getName().matches(regex))
                .toList();
        }
        
        @Override
        public long count() {
            return users.size();
        }
    }
}

