package com.xink.training.day13.service;

public class PolicyResult {
    private final boolean valid;
    private final String message;
    
    public PolicyResult(boolean valid, String message) {
        this.valid = valid;
        this.message = message;
    }
    
    public boolean isValid() {
        return valid;
    }
    
    public String getMessage() {
        return message;
    }
}

