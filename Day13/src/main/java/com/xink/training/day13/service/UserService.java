package com.xink.training.day13.service;

import com.xink.training.day13.model.User;

import java.util.List;
import java.util.Optional;

public class UserService {
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final AuditService auditService;
    
    public UserService(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.auditService = null;
    }
    
    public UserService(UserRepository userRepository, EmailService emailService, AuditService auditService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.auditService = auditService;
    }
    
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    public User createUser(User user) {
        return userRepository.save(user);
    }
    
    public User createUserWithAudit(User user) {
        User saved = userRepository.save(user);
        if (auditService != null) {
            auditService.logEvent(new AuditEvent("CREATE_USER", "User"));
        }
        return saved;
    }
    
    public List<User> findUsersByAge(int minAge, int maxAge) {
        return userRepository.findByAgeRange(minAge, maxAge);
    }
    
    public List<User> findUsersByNamePattern(String pattern) {
        return userRepository.findByNamePattern(pattern);
    }
    
    public UserRegistrationResponse registerUser(UserRegistrationRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("User with email " + request.getEmail() + " already exists");
        }
        
        User user = new User(request.getName(), request.getEmail());
        User saved = userRepository.save(user);
        
        if (emailService != null) {
            emailService.sendWelcomeEmail(saved.getEmail(), saved.getName());
        }
        
        return new UserRegistrationResponse(saved.getId(), saved.getEmail());
    }
    
    public UserRegistrationResponse registerUser(String name, String email) {
        UserRegistrationRequest request = new UserRegistrationRequest(name, email, "Password123!");
        return registerUser(request);
    }
    
    public AuthenticationResult authenticate(AuthenticationRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        if (userOpt.isEmpty()) {
            return new AuthenticationResult(false, "Invalid credentials", null);
        }
        
        // Simplified authentication - in real app would check password hash
        return new AuthenticationResult(true, null, "mock-token-" + request.getEmail());
    }
    
    public UserProfile updateProfile(Long userId, UserProfileUpdateRequest request) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new UserNotFoundException("User with ID " + userId + " not found");
        }
        
        User user = userOpt.get();
        if (request.getName() != null) {
            user.setName(request.getName());
        }
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }
        if (request.getDateOfBirth() != null) {
            user.setDateOfBirth(request.getDateOfBirth());
        }
        
        User saved = userRepository.save(user);
        return new UserProfile(saved.getName(), saved.getEmail(), saved.getPhone(), saved.getDateOfBirth());
    }
}

