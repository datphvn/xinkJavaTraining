package com.xink.training.day13.service;

import java.util.regex.Pattern;

public class EmailValidator {
    private static final String EMAIL_PATTERN = 
        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    
    public boolean isValid(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        
        // Check for double dots
        if (email.contains("..")) {
            return false;
        }
        
        return pattern.matcher(email).matches();
    }
}

