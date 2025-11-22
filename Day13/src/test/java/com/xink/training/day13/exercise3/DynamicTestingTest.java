package com.xink.training.day13.exercise3;

import com.xink.training.day13.service.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@DisplayName("Dynamic Testing Examples")
class DynamicTestingTest {
    
    @TestFactory
    @DisplayName("Should dynamically test mathematical operations")
    Collection<DynamicTest> dynamicMathTests() {
        Calculator calculator = new Calculator();
        
        List<int[]> testData = Arrays.asList(
            new int[]{2, 3, 5},    // add
            new int[]{10, 4, 6},   // subtract  
            new int[]{5, 6, 30},   // multiply
            new int[]{20, 4, 5}    // divide
        );
        
        String[] operations = {"add", "subtract", "multiply", "divide"};
        
        List<DynamicTest> tests = new ArrayList<>();
        
        for (int i = 0; i < testData.size(); i++) {
            int[] data = testData.get(i);
            String operation = operations[i];
            
            String testName = String.format("Should %s %d and %d to get %d", 
                operation, data[0], data[1], data[2]);
            
            DynamicTest dynamicTest = dynamicTest(testName, () -> {
                double result = switch (operation) {
                    case "add" -> calculator.add(data[0], data[1]);
                    case "subtract" -> calculator.subtract(data[0], data[1]);
                    case "multiply" -> calculator.multiply(data[0], data[1]);
                    case "divide" -> calculator.divide(data[0], data[1]);
                    default -> throw new IllegalArgumentException("Unknown operation: " + operation);
                };
                
                assertEquals(data[2], result, 0.001);
            });
            
            tests.add(dynamicTest);
        }
        
        return tests;
    }
    
    @TestFactory
    @DisplayName("Should validate various email formats")
    Stream<DynamicTest> emailValidationTests() {
        EmailValidator validator = new EmailValidator();
        
        Map<String, Boolean> emailTestCases = Map.of(
            "valid@example.com", true,
            "user.name@domain.org", true,
            "test+tag@subdomain.example.co.uk", true,
            "invalid-email", false,
            "user@", false,
            "@domain.com", false,
            "user..double.dot@example.com", false
        );
        
        return emailTestCases.entrySet().stream()
            .map(entry -> dynamicTest(
                String.format("Email '%s' should be %s", 
                    entry.getKey(), entry.getValue() ? "valid" : "invalid"),
                () -> {
                    boolean result = validator.isValid(entry.getKey());
                    assertEquals(entry.getValue(), result,
                        String.format("Email validation failed for: %s", entry.getKey()));
                }
            ));
    }
    
    @TestFactory
    @DisplayName("Should test date calculations for different years")
    Iterator<DynamicTest> dateCalculationTests() {
        DateCalculator calculator = new DateCalculator();
        
        List<DynamicTest> tests = new ArrayList<>();
        
        // Generate tests for leap years
        int[] leapYears = {2000, 2004, 2008, 2012, 2016, 2020, 2024};
        for (int year : leapYears) {
            tests.add(dynamicTest(
                String.format("Year %d should be a leap year", year),
                () -> assertTrue(calculator.isLeapYear(year), 
                    "Year " + year + " should be identified as leap year")
            ));
        }
        
        // Generate tests for non-leap years
        int[] nonLeapYears = {1900, 2001, 2002, 2003, 2100, 2200, 2300};
        for (int year : nonLeapYears) {
            tests.add(dynamicTest(
                String.format("Year %d should not be a leap year", year),
                () -> assertFalse(calculator.isLeapYear(year),
                    "Year " + year + " should not be identified as leap year")
            ));
        }
        
        return tests.iterator();
    }
    
    @TestFactory
    @DisplayName("Should test password policies dynamically")
    Stream<DynamicTest> passwordPolicyTests() {
        PasswordPolicyChecker checker = new PasswordPolicyChecker();
        
        // Define password policies to test
        Map<String, PasswordPolicy> policies = Map.of(
            "Basic Policy", new PasswordPolicy(8, false, false, false),
            "Medium Policy", new PasswordPolicy(10, true, true, false),
            "Strong Policy", new PasswordPolicy(12, true, true, true)
        );
        
        // Test passwords
        List<String> testPasswords = Arrays.asList(
            "simple123",
            "Complex123!",
            "VeryStrong&P@ssw0rd123",
            "weak",
            "NoDigits!"
        );
        
        return policies.entrySet().stream()
            .flatMap(policyEntry -> testPasswords.stream()
                .map(password -> {
                    String testName = String.format("Password '%s' with %s", 
                        password, policyEntry.getKey());
                    
                    return dynamicTest(testName, () -> {
                        PolicyResult result = checker.checkPassword(password, policyEntry.getValue());
                        
                        // Dynamic assertion based on password and policy
                        boolean expectedResult = evaluatePasswordAgainstPolicy(password, policyEntry.getValue());
                        assertEquals(expectedResult, result.isValid(),
                            String.format("Password '%s' policy check failed for %s", 
                                password, policyEntry.getKey()));
                    });
                })
            );
    }
    
    private boolean evaluatePasswordAgainstPolicy(String password, PasswordPolicy policy) {
        if (password.length() < policy.getMinLength()) {
            return false;
        }
        
        if (policy.requiresUppercase() && !password.matches(".*[A-Z].*")) {
            return false;
        }
        
        if (policy.requiresDigits() && !password.matches(".*\\d.*")) {
            return false;
        }
        
        if (policy.requiresSpecialChars() && !password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) {
            return false;
        }
        
        return true;
    }
}

