package com.xink.training.day13.service;

public class PasswordPolicy {
    private final int minLength;
    private final boolean requiresUppercase;
    private final boolean requiresDigits;
    private final boolean requiresSpecialChars;
    
    public PasswordPolicy(int minLength, boolean requiresUppercase, 
                         boolean requiresDigits, boolean requiresSpecialChars) {
        this.minLength = minLength;
        this.requiresUppercase = requiresUppercase;
        this.requiresDigits = requiresDigits;
        this.requiresSpecialChars = requiresSpecialChars;
    }
    
    public int getMinLength() {
        return minLength;
    }
    
    public boolean requiresUppercase() {
        return requiresUppercase;
    }
    
    public boolean requiresDigits() {
        return requiresDigits;
    }
    
    public boolean requiresSpecialChars() {
        return requiresSpecialChars;
    }
}

