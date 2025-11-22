package com.xink.training.day13.service;

import com.xink.training.day13.model.User;

public class UserProcessor {
    
    public String determineStatus(User user) {
        if (user == null) {
            return "INVALID";
        }
        
        if (user.getAge() < 18) {
            return "MINOR";
        } else if (user.getAge() >= 65) {
            return "SENIOR";
        } else {
            return "ADULT";
        }
    }
}

