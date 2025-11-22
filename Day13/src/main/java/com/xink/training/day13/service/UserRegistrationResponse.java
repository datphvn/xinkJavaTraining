package com.xink.training.day13.service;

public class UserRegistrationResponse {
    private Long userId;
    private String email;
    
    public UserRegistrationResponse(Long userId, String email) {
        this.userId = userId;
        this.email = email;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public String getEmail() {
        return email;
    }
}

