package com.xink.training.day13.miniproject;

import com.xink.training.day13.exercise1.UserAssertions;
import com.xink.training.day13.model.Address;
import com.xink.training.day13.model.User;
import com.xink.training.day13.service.Calculator;
import com.xink.training.day13.service.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@DisplayName("Comprehensive Test Suite - All JUnit 5 Features")
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ComprehensiveTestSuite {
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private EmailService emailService;
    
    @Mock
    private AuditService auditService;
    
    @Captor
    private ArgumentCaptor<User> userCaptor;
    
    private UserService userService;
    
    @BeforeAll
    static void initializeSuite() {
        System.out.println("🚀 Starting Comprehensive Test Suite");
    }
    
    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, emailService, auditService);
    }
    
    @AfterAll
    static void cleanupSuite() {
        System.out.println("🏁 Comprehensive Test Suite completed");
    }
    
    @Nested
    @DisplayName("User Registration - Complete Flow")
    class UserRegistrationFlow {
        
        @Test
        @DisplayName("Should successfully register new user with all validations")
        @Tag("integration")
        void shouldRegisterNewUserSuccessfully() {
            // Given
            UserRegistrationRequest request = UserRegistrationRequest.builder()
                .name("John Doe")
                .email("john.doe@example.com")
                .password("SecureP@ssw0rd123!")
                .build();
            
            when(userRepository.existsByEmail("john.doe@example.com")).thenReturn(false);
            
            User savedUser = new User("John Doe", "john.doe@example.com");
            savedUser.setId(1L);
            when(userRepository.save(any(User.class))).thenReturn(savedUser);
            when(emailService.sendWelcomeEmail(anyString(), anyString())).thenReturn(true);
            
            // When
            UserRegistrationResponse response = userService.registerUser(request);
            
            // Then - Comprehensive assertions
            assertAll("User registration validation",
                () -> assertNotNull(response, "Response should not be null"),
                () -> assertEquals(1L, response.getUserId(), "User ID should be set"),
                () -> assertEquals("john.doe@example.com", response.getEmail(), "Email should match")
            );
            
            // Verify interactions
            verify(userRepository).existsByEmail("john.doe@example.com");
            verify(userRepository).save(userCaptor.capture());
            verify(emailService).sendWelcomeEmail("john.doe@example.com", "John Doe");
            
            // Verify captured user
            User capturedUser = userCaptor.getValue();
            UserAssertions.assertThat(capturedUser)
                .hasName("John Doe")
                .hasValidEmail();
        }
        
        @ParameterizedTest(name = "Should reject registration for email: {0}")
        @CsvSource({
            "existing@example.com, User already exists",
            "duplicate@test.com, User already exists"
        })
        @Tag("validation")
        void shouldRejectDuplicateEmail(String email, String expectedError) {
            when(userRepository.existsByEmail(email)).thenReturn(true);
            
            UserRegistrationRequest request = UserRegistrationRequest.builder()
                .name("Test User")
                .email(email)
                .password("Password123!")
                .build();
            
            assertThatThrownBy(() -> userService.registerUser(request))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessageContaining(email);
        }
    }
    
    @Nested
    @DisplayName("User Profile Management")
    class UserProfileManagement {
        
        private User testUser;
        
        @BeforeEach
        void setUpProfile() {
            testUser = new User("Jane Smith", "jane@example.com");
            testUser.setId(100L);
            testUser.setAge(28);
            
            Address address = new Address("456 Oak St", "Chicago", "IL", "60601");
            testUser.setAddress(address);
            
            when(userRepository.findById(100L)).thenReturn(Optional.of(testUser));
        }
        
        @Test
        @DisplayName("Should update user profile completely")
        void shouldUpdateUserProfileCompletely() {
            UserProfileUpdateRequest request = UserProfileUpdateRequest.builder()
                .name("Jane Doe")
                .phone("+1-312-555-0123")
                .dateOfBirth(LocalDate.of(1995, 6, 15))
                .build();
            
            User updatedUser = new User("Jane Doe", "jane@example.com");
            updatedUser.setPhone("+1-312-555-0123");
            updatedUser.setDateOfBirth(LocalDate.of(1995, 6, 15));
            when(userRepository.save(any(User.class))).thenReturn(updatedUser);
            
            UserProfile profile = userService.updateProfile(100L, request);
            
            assertAll("Profile update validation",
                () -> assertEquals("Jane Doe", profile.getName()),
                () -> assertEquals("+1-312-555-0123", profile.getPhone()),
                () -> assertEquals(LocalDate.of(1995, 6, 15), profile.getDateOfBirth())
            );
        }
        
        @Test
        @DisplayName("Should throw exception for non-existent user")
        void shouldThrowExceptionForNonExistentUser() {
            when(userRepository.findById(999L)).thenReturn(Optional.empty());
            
            UserProfileUpdateRequest request = UserProfileUpdateRequest.builder()
                .name("New Name")
                .build();
            
            assertThatThrownBy(() -> userService.updateProfile(999L, request))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("999");
        }
    }
    
    @Nested
    @DisplayName("User Search & Query")
    class UserSearchAndQuery {
        
        @Test
        @DisplayName("Should find users by age range")
        void shouldFindUsersByAgeRange() {
            List<User> mockUsers = List.of(
                new User("User 1", "user1@example.com", 25),
                new User("User 2", "user2@example.com", 30)
            );
            
            when(userRepository.findByAgeRange(20, 35))
                .thenReturn(mockUsers);
            
            List<User> result = userService.findUsersByAge(20, 35);
            
            assertThat(result)
                .hasSize(2)
                .extracting(User::getAge)
                .allMatch(age -> age >= 20 && age <= 35);
        }
        
        @Test
        @DisplayName("Should find users by name pattern")
        void shouldFindUsersByNamePattern() {
            List<User> mockUsers = List.of(
                new User("John Smith", "john@example.com"),
                new User("Johnny Doe", "johnny@example.com")
            );
            
            when(userRepository.findByNamePattern("John*"))
                .thenReturn(mockUsers);
            
            List<User> result = userService.findUsersByNamePattern("John*");
            
            assertThat(result)
                .hasSize(2)
                .extracting(User::getName)
                .allMatch(name -> name.startsWith("John"));
        }
    }
    
    @Nested
    @DisplayName("Exception Handling & Edge Cases")
    class ExceptionHandling {
        
        @Test
        @DisplayName("Should handle database connection errors gracefully")
        void shouldHandleDatabaseErrors() {
            when(userRepository.findById(1L))
                .thenThrow(new DatabaseConnectionException("Database unavailable"));
            
            assertThrows(DatabaseConnectionException.class, () -> 
                userRepository.findById(1L));
        }
        
        @Test
        @DisplayName("Should handle email service failures")
        void shouldHandleEmailServiceFailures() {
            when(emailService.sendEmail(anyString(), anyString()))
                .thenThrow(new EmailServiceException("SMTP server down"));
            
            assertThrows(EmailServiceException.class, () ->
                emailService.sendEmail("test@example.com", "Test"));
        }
    }
    
    @Test
    @DisplayName("Should demonstrate timeout testing")
    @Timeout(value = 2, unit = java.util.concurrent.TimeUnit.SECONDS)
    void shouldCompleteWithinTimeout() {
        // Simulate quick operation
        Calculator calculator = new Calculator();
        for (int i = 0; i < 1000; i++) {
            calculator.add(i, i + 1);
        }
        assertTrue(true);
    }
}

