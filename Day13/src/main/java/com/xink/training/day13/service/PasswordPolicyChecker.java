package com.xink.training.day13.service;

public class PasswordPolicyChecker {
    
    public PolicyResult checkPassword(String password, PasswordPolicy policy) {
        if (password == null || password.length() < policy.getMinLength()) {
            return new PolicyResult(false, "Password too short");
        }
        
        if (policy.requiresUppercase() && !password.matches(".*[A-Z].*")) {
            return new PolicyResult(false, "Password must contain uppercase letter");
        }
        
        if (policy.requiresDigits() && !password.matches(".*\\d.*")) {
            return new PolicyResult(false, "Password must contain digit");
        }
        
        if (policy.requiresSpecialChars() && !password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) {
            return new PolicyResult(false, "Password must contain special character");
        }
        
        return new PolicyResult(true, "Password meets policy requirements");
    }
}

