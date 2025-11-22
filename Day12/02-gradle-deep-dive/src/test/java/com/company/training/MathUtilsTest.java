package com.company.training;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("MathUtils Tests")
class MathUtilsTest {
    
    @Test
    @DisplayName("Should calculate circle area correctly")
    void shouldCalculateCircleAreaCorrectly() {
        double area = MathUtils.calculateCircleArea(5.0);
        assertThat(area).isCloseTo(78.54, org.assertj.core.data.Offset.offset(0.01));
    }
    
    @Test
    @DisplayName("Should throw exception for negative radius")
    void shouldThrowExceptionForNegativeRadius() {
        assertThrows(IllegalArgumentException.class, 
            () -> MathUtils.calculateCircleArea(-1.0));
    }
    
    @ParameterizedTest
    @DisplayName("Should identify prime numbers correctly")
    @ValueSource(ints = {2, 3, 5, 7, 11, 13, 17, 19})
    void shouldIdentifyPrimeNumbers(int number) {
        assertTrue(MathUtils.isPrime(number), 
            number + " should be identified as prime");
    }
    
    @ParameterizedTest
    @DisplayName("Should identify non-prime numbers correctly")
    @ValueSource(ints = {1, 4, 6, 8, 9, 10, 12, 14, 15, 16, 18, 20})
    void shouldIdentifyNonPrimeNumbers(int number) {
        assertFalse(MathUtils.isPrime(number), 
            number + " should be identified as non-prime");
    }
    
    @Test
    @DisplayName("Should calculate factorial correctly")
    void shouldCalculateFactorialCorrectly() {
        assertEquals(1, MathUtils.factorial(0));
        assertEquals(1, MathUtils.factorial(1));
        assertEquals(6, MathUtils.factorial(3));
        assertEquals(120, MathUtils.factorial(5));
    }
    
    @Test
    @DisplayName("Should throw exception for negative factorial")
    void shouldThrowExceptionForNegativeFactorial() {
        assertThrows(IllegalArgumentException.class, 
            () -> MathUtils.factorial(-1));
    }
}

