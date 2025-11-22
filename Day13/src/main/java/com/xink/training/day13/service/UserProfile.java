package com.xink.training.day13.service;

import java.time.LocalDate;

public class UserProfile {
    private String name;
    private String email;
    private String phone;
    private LocalDate dateOfBirth;
    
    public UserProfile(String name, String email, String phone, LocalDate dateOfBirth) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.dateOfBirth = dateOfBirth;
    }
    
    public String getName() {
        return name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
}

