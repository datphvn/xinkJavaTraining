package com.xink.training.day13.exercise4;

import com.xink.training.day13.service.Calculator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Performance Test Suite")
class PerformanceTest {
    
    @Test
    @Tag("unit")
    @Tag("fast")
    @DisplayName("Should perform quick unit operation")
    void quickUnitTest() {
        // Fast unit test
        Calculator calculator = new Calculator();
        assertEquals(5, calculator.add(2, 3));
    }
    
    @Test  
    @Tag("integration")
    @Tag("slow")
    @DisplayName("Should perform database integration test")
    void databaseIntegrationTest() {
        // Slower integration test requiring database
        // This would be excluded from quick test runs
        assertTrue(true);
    }
    
    @Test
    @Tag("performance")
    @Tag("slow")
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    @DisplayName("Should complete performance test within time limit")
    void performanceTest() {
        // Performance test với timeout
        Calculator calculator = new Calculator();
        for (int i = 0; i < 1000; i++) {
            calculator.add(i, i + 1);
        }
        assertTrue(true);
    }
    
    @Test
    @Tag("smoke")
    @DisplayName("Should pass basic smoke test")
    void smokeTest() {
        // Critical functionality test for CI/CD pipeline
        Calculator calculator = new Calculator();
        assertTrue(calculator.add(1, 1) == 2, "Basic calculation should work");
    }
}

