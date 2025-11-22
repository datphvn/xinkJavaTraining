package com.xink.training.day13.service;

public class AuthenticationResult {
    private boolean success;
    private String errorMessage;
    private String token;
    
    public AuthenticationResult(boolean success, String errorMessage, String token) {
        this.success = success;
        this.errorMessage = errorMessage;
        this.token = token;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public String getToken() {
        return token;
    }
}

