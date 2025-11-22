package com.xink.training.day13.exercise4;

import com.xink.training.day13.model.User;
import com.xink.training.day13.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@DisplayName("User Service Tests")
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    
    private UserService userService;
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private EmailService emailService;
    
    @Mock
    private AuditService auditService;
    
    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, emailService, auditService);
    }
    
    @Nested
    @DisplayName("User Registration Tests")
    class UserRegistrationTests {
        
        @BeforeEach
        void setUpRegistration() {
            // Setup specific to registration tests
            when(userRepository.existsByEmail(anyString())).thenReturn(false);
        }
        
        @Test
        @DisplayName("Should register user with valid data")
        void shouldRegisterUserWithValidData() {
            // Given
            UserRegistrationRequest request = UserRegistrationRequest.builder()
                .name("John Doe")
                .email("john@example.com")
                .password("SecurePassword123!")
                .build();
            
            User savedUser = new User("John Doe", "john@example.com");
            savedUser.setId(1L);
            when(userRepository.save(any(User.class))).thenReturn(savedUser);
            when(emailService.sendWelcomeEmail(anyString(), anyString())).thenReturn(true);
            
            // When
            UserRegistrationResponse response = userService.registerUser(request);
            
            // Then
            assertThat(response)
                .isNotNull()
                .extracting(UserRegistrationResponse::getUserId, UserRegistrationResponse::getEmail)
                .containsExactly(1L, "john@example.com");
            
            verify(userRepository).save(any(User.class));
            verify(emailService).sendWelcomeEmail(eq("john@example.com"), eq("John Doe"));
        }
        
        @Test
        @DisplayName("Should throw exception when email already exists")
        void shouldThrowExceptionWhenEmailAlreadyExists() {
            // Given
            when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);
            
            UserRegistrationRequest request = UserRegistrationRequest.builder()
                .name("Jane Doe")
                .email("existing@example.com")
                .password("Password123!")
                .build();
            
            // When & Then
            assertThatThrownBy(() -> userService.registerUser(request))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessageContaining("existing@example.com");
            
            verify(userRepository, never()).save(any(User.class));
            verify(emailService, never()).sendWelcomeEmail(anyString(), anyString());
        }
        
        @Nested
        @DisplayName("Password Validation Tests")
        class PasswordValidationTests {
            
            @Test
            @DisplayName("Should accept strong password")
            void shouldAcceptStrongPassword() {
                UserRegistrationRequest request = UserRegistrationRequest.builder()
                    .name("John Doe")
                    .email("john@example.com")
                    .password("StrongP@ssw0rd123!")
                    .build();
                
                User savedUser = new User("John Doe", "john@example.com");
                when(userRepository.save(any(User.class)))
                    .thenReturn(savedUser);
                when(emailService.sendWelcomeEmail(anyString(), anyString())).thenReturn(true);
                
                assertDoesNotThrow(() -> userService.registerUser(request));
            }
        }
    }
    
    @Nested
    @DisplayName("User Authentication Tests")
    class UserAuthenticationTests {
        
        @BeforeEach
        void setUpAuthentication() {
            // Setup specific to authentication tests
            User existingUser = new User("John Doe", "john@example.com");
            existingUser.setPasswordHash("$2a$10$encrypted.password.hash");
            when(userRepository.findByEmail("john@example.com"))
                .thenReturn(Optional.of(existingUser));
        }
        
        @Test
        @DisplayName("Should authenticate user with correct credentials")
        void shouldAuthenticateUserWithCorrectCredentials() {
            // Given
            AuthenticationRequest request = new AuthenticationRequest(
                "john@example.com", "correctPassword");
            
            // When
            AuthenticationResult result = userService.authenticate(request);
            
            // Then
            assertThat(result)
                .extracting(AuthenticationResult::isSuccess, AuthenticationResult::getToken)
                .satisfies(success -> assertThat(success).isTrue(),
                          token -> assertThat(token).isNotEmpty());
        }
        
        @Test
        @DisplayName("Should fail authentication with wrong password")
        void shouldFailAuthenticationWithWrongPassword() {
            // Given
            AuthenticationRequest request = new AuthenticationRequest(
                "john@example.com", "wrongPassword");
            
            // When
            AuthenticationResult result = userService.authenticate(request);
            
            // Then
            assertThat(result.isSuccess()).isFalse();
            assertThat(result.getErrorMessage()).contains("Invalid credentials");
        }
        
        @Nested
        @DisplayName("Account Lockout Tests")
        class AccountLockoutTests {
            
            @Test
            @DisplayName("Should handle authentication attempts")
            void shouldHandleAuthenticationAttempts() {
                AuthenticationRequest request = new AuthenticationRequest(
                    "john@example.com", "wrongPassword");
                
                // Multiple attempts
                AuthenticationResult result1 = userService.authenticate(request);
                AuthenticationResult result2 = userService.authenticate(request);
                
                assertThat(result1.isSuccess()).isFalse();
                assertThat(result2.isSuccess()).isFalse();
            }
        }
    }
    
    @Nested
    @DisplayName("User Profile Tests")
    class UserProfileTests {
        
        private User testUser;
        
        @BeforeEach
        void setUpProfile() {
            testUser = new User("John Doe", "john@example.com");
            testUser.setId(1L);
            when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        }
        
        @Test
        @DisplayName("Should update user profile with valid data")
        void shouldUpdateUserProfileWithValidData() {
            UserProfileUpdateRequest request = UserProfileUpdateRequest.builder()
                .name("John Smith")
                .phone("+1-555-0123")
                .dateOfBirth(LocalDate.of(1990, 1, 15))
                .build();
            
            User updatedUser = new User("John Smith", "john@example.com");
            updatedUser.setPhone("+1-555-0123");
            updatedUser.setDateOfBirth(LocalDate.of(1990, 1, 15));
            when(userRepository.save(any(User.class))).thenReturn(updatedUser);
            
            UserProfile result = userService.updateProfile(1L, request);
            
            assertThat(result)
                .extracting(UserProfile::getName, UserProfile::getPhone)
                .containsExactly("John Smith", "+1-555-0123");
        }
        
        @Test
        @DisplayName("Should throw exception when user not found")
        void shouldThrowExceptionWhenUserNotFound() {
            when(userRepository.findById(999L)).thenReturn(Optional.empty());
            
            UserProfileUpdateRequest request = UserProfileUpdateRequest.builder()
                .name("New Name")
                .build();
            
            assertThatThrownBy(() -> userService.updateProfile(999L, request))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("999");
        }
    }
}

