package com.xink.training.day13.exercise2;

import com.xink.training.day13.service.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Advanced Parameterized Testing Patterns")
class AdvancedParameterizedTest {
    
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 5, 8, 13, 21, 34})
    void shouldGenerateFibonacciNumbers(int expected) {
        FibonacciGenerator generator = new FibonacciGenerator();
        List<Integer> sequence = generator.generate(8);
        
        assertTrue(sequence.contains(expected));
    }
    
    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
        "John Doe | john.doe@example.com | VALID",
        "Jane Smith | jane.smith@company.org | VALID", 
        "Invalid User | not-an-email | INVALID",
        "Another User | user@domain | INVALID"
    })
    void shouldValidateUserWithCustomDelimiter(String name, String email, String expectedResult) {
        UserValidator validator = new UserValidator();
        ValidationResult result = validator.validateUser(name, email);
        
        assertEquals(ValidationResult.valueOf(expectedResult), result);
    }
    
    // Parameterized test với custom display names
    @ParameterizedTest(name = "Password ''{0}'' should be {1}")
    @MethodSource("providePasswordStrengthData")
    void shouldEvaluatePasswordStrength(String password, PasswordStrength expectedStrength) {
        PasswordEvaluator evaluator = new PasswordEvaluator();
        PasswordStrength actualStrength = evaluator.evaluate(password);
        
        assertEquals(expectedStrength, actualStrength,
            String.format("Password '%s' should have strength %s", password, expectedStrength));
    }
    
    private static Stream<Arguments> providePasswordStrengthData() {
        return Stream.of(
            Arguments.of("123", PasswordStrength.WEAK),
            Arguments.of("password", PasswordStrength.WEAK),
            Arguments.of("Password123", PasswordStrength.MEDIUM),
            Arguments.of("P@ssw0rd123!", PasswordStrength.STRONG),
            Arguments.of("MyVeryStrong&ComplexP@ssw0rd2024!", PasswordStrength.VERY_STRONG)
        );
    }
    
    // Combining multiple parameter sources
    @ParameterizedTest
    @MethodSource("provideHttpStatusTestData")
    void shouldHandleHttpStatus(int statusCode, String expectedMessage, boolean isError) {
        HttpResponseHandler handler = new HttpResponseHandler();
        HttpResponse response = new HttpResponse(statusCode, expectedMessage);
        
        assertEquals(expectedMessage, handler.getMessage(response));
        assertEquals(isError, handler.isError(response));
        assertTrue(handler.isValidStatus(statusCode));
    }
    
    private static Stream<Arguments> provideHttpStatusTestData() {
        return Stream.of(
            Arguments.of(200, "OK", false),
            Arguments.of(201, "Created", false), 
            Arguments.of(400, "Bad Request", true),
            Arguments.of(401, "Unauthorized", true),
            Arguments.of(404, "Not Found", true),
            Arguments.of(500, "Internal Server Error", true)
        );
    }
}

