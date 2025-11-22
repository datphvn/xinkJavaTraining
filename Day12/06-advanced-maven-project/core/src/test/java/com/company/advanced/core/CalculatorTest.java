package com.company.advanced.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Calculator Tests")
class CalculatorTest {
    
    private final Calculator calculator = new Calculator();
    
    @Test
    @DisplayName("Should add two positive numbers correctly")
    void shouldAddTwoPositiveNumbers() {
        assertEquals(5, calculator.add(2, 3));
    }
    
    @Test
    @DisplayName("Should subtract two numbers correctly")
    void shouldSubtractTwoNumbers() {
        assertEquals(1, calculator.subtract(3, 2));
    }
    
    @Test
    @DisplayName("Should multiply two numbers correctly")
    void shouldMultiplyTwoNumbers() {
        assertEquals(6, calculator.multiply(2, 3));
    }
    
    @Test
    @DisplayName("Should divide two numbers correctly")
    void shouldDivideTwoNumbers() {
        assertEquals(2.0, calculator.divide(6, 3), 0.001);
    }
    
    @Test
    @DisplayName("Should throw exception when dividing by zero")
    void shouldThrowExceptionWhenDividingByZero() {
        assertThrows(IllegalArgumentException.class, 
            () -> calculator.divide(5, 0));
    }
}

