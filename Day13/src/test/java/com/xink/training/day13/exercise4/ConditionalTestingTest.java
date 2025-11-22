package com.xink.training.day13.exercise4;

import com.xink.training.day13.service.Calculator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Conditional Testing Examples")
class ConditionalTestingTest {
    
    @Test
    @EnabledOnOs(OS.LINUX)
    @DisplayName("Should run only on Linux")
    void linuxSpecificTest() {
        // Test that only runs on Linux systems
        Calculator calculator = new Calculator();
        assertNotNull(calculator);
    }
    
    @Test
    @DisabledOnOs(OS.WINDOWS)
    @DisplayName("Should not run on Windows")
    void nonWindowsTest() {
        // Test that runs on all systems except Windows
        Calculator calculator = new Calculator();
        assertNotNull(calculator);
    }
    
    @Test
    @EnabledOnJre({JRE.JAVA_17, JRE.JAVA_21})
    @DisplayName("Should run on Java 17 and 21 only")
    void modernJavaTest() {
        // Test that uses modern Java features
        Calculator calculator = new Calculator();
        assertNotNull(calculator);
    }
    
    @Test
    @EnabledIfSystemProperty(named = "test.integration.enabled", matches = "true")
    @DisplayName("Should run when integration tests are enabled")
    void conditionalIntegrationTest() {
        // Run with -Dtest.integration.enabled=true
        Calculator calculator = new Calculator();
        assertNotNull(calculator);
    }
    
    @Test
    @EnabledIfEnvironmentVariable(named = "DATABASE_URL", matches = ".*localhost.*")
    @DisplayName("Should run when connected to local database")
    void localDatabaseTest() {
        // Only run when DATABASE_URL contains localhost
        Calculator calculator = new Calculator();
        assertNotNull(calculator);
    }
    
    @Test
    @EnabledIf("customCondition")
    @DisplayName("Should run based on custom condition")
    void customConditionalTest() {
        // Test runs only when custom condition is met
        Calculator calculator = new Calculator();
        assertNotNull(calculator);
    }
    
    // Custom condition method
    static boolean customCondition() {
        // Custom logic to determine if test should run
        String profile = System.getProperty("test.profile", "default");
        return "development".equals(profile) || "testing".equals(profile);
    }
    
    @Test
    @EnabledIfSystemProperty(named = "java.vendor", matches = ".*OpenJDK.*")
    @DisplayName("Should run on OpenJDK")
    void openJdkSpecificTest() {
        // Test specific to OpenJDK implementation
        Calculator calculator = new Calculator();
        assertNotNull(calculator);
    }
}

