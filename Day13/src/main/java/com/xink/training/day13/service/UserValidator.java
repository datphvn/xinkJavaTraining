package com.xink.training.day13.service;

import com.xink.training.day13.model.User;

public class UserValidator {
    
    public boolean validate(User user) {
        if (user == null) {
            return false;
        }
        
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            return false;
        }
        
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            return false;
        }
        
        if (user.getAge() < 0 || user.getAge() > 150) {
            return false;
        }
        
        return true;
    }
    
    public ValidationResult validateUser(String name, String email) {
        if (name == null || name.trim().isEmpty()) {
            return ValidationResult.INVALID;
        }
        
        EmailValidator emailValidator = new EmailValidator();
        if (!emailValidator.isValid(email)) {
            return ValidationResult.INVALID;
        }
        
        return ValidationResult.VALID;
    }
}

