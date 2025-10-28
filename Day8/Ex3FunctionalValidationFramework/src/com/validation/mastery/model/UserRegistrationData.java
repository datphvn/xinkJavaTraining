package com.validation.mastery.model;

import java.math.BigDecimal;

// Model dữ liệu mẫu để validation
public record UserRegistrationData(
        String username,
        String password,
        String confirmPassword,
        String email,
        int age,
        BigDecimal accountBalance
) {}
