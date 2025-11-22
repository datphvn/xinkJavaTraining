package com.xink.training.day13.service;

import java.time.LocalDate;

public class UserProfileUpdateRequest {
    private String name;
    private String phone;
    private LocalDate dateOfBirth;
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
    
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private String name;
        private String phone;
        private LocalDate dateOfBirth;
        
        public Builder name(String name) {
            this.name = name;
            return this;
        }
        
        public Builder phone(String phone) {
            this.phone = phone;
            return this;
        }
        
        public Builder dateOfBirth(LocalDate dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
            return this;
        }
        
        public UserProfileUpdateRequest build() {
            UserProfileUpdateRequest request = new UserProfileUpdateRequest();
            request.setName(name);
            request.setPhone(phone);
            request.setDateOfBirth(dateOfBirth);
            return request;
        }
    }
}

