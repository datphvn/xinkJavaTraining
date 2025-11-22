package com.xink.training.day13.service;

public interface EmailService {
    boolean sendWelcomeEmail(String email, String name);
    boolean sendEmail(String to, String subject);
}

