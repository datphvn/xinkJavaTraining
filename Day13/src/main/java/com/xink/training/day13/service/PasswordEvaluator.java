package com.xink.training.day13.service;

public class PasswordEvaluator {
    
    public PasswordStrength evaluate(String password) {
        if (password == null || password.length() < 3) {
            return PasswordStrength.WEAK;
        }
        
        if (password.length() < 8) {
            return PasswordStrength.WEAK;
        }
        
        boolean hasUpper = password.matches(".*[A-Z].*");
        boolean hasLower = password.matches(".*[a-z].*");
        boolean hasDigit = password.matches(".*\\d.*");
        boolean hasSpecial = password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*");
        
        int strength = 0;
        if (hasUpper) strength++;
        if (hasLower) strength++;
        if (hasDigit) strength++;
        if (hasSpecial) strength++;
        
        if (password.length() >= 8 && strength >= 2) {
            if (password.length() >= 12 && strength >= 4) {
                return PasswordStrength.VERY_STRONG;
            } else if (strength >= 3) {
                return PasswordStrength.STRONG;
            } else {
                return PasswordStrength.MEDIUM;
            }
        }
        
        return PasswordStrength.WEAK;
    }
}

