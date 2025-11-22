package com.xink.training.day13.service;

public class AuthenticationRequest {
    private String email;
    private String password;
    
    public AuthenticationRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getPassword() {
        return password;
    }
}

