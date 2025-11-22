package com.company.training.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @GetMapping("/hello")
    public String hello() {
        return "Hello from Training Web Module!";
    }
}

