package com.xink.training.day13.exercise2;

import com.xink.training.day13.model.User;
import com.xink.training.day13.service.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.time.DayOfWeek;
import java.time.Month;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Parameterized Testing Examples")
class ParameterizedTestingTest {
    
    @ParameterizedTest(name = "Test {index}: {0} + {1} = {2}")
    @DisplayName("Should add numbers correctly")
    @CsvSource({
        "1, 2, 3",
        "10, 20, 30", 
        "-5, 5, 0",
        "0, 0, 0",
        "100, -50, 50"
    })
    void shouldAddNumbersCorrectly(int a, int b, int expected) {
        Calculator calculator = new Calculator();
        int result = calculator.add(a, b);
        assertEquals(expected, result, 
            String.format("Adding %d + %d should equal %d", a, b, expected));
    }
    
    @ParameterizedTest(name = "Email validation: {0}")
    @ValueSource(strings = {
        "user@example.com",
        "test.email@domain.co.uk", 
        "user.name+tag@example.org",
        "admin@subdomain.example.com"
    })
    void shouldValidateCorrectEmails(String email) {
        EmailValidator validator = new EmailValidator();
        assertTrue(validator.isValid(email), 
            "Email should be valid: " + email);
    }
    
    @ParameterizedTest(name = "Invalid email: {0}")
    @ValueSource(strings = {
        "invalid-email",
        "@domain.com",
        "user@",
        "user..name@domain.com",
        ""
    })
    void shouldRejectInvalidEmails(String email) {
        EmailValidator validator = new EmailValidator();
        assertFalse(validator.isValid(email), 
            "Email should be invalid: " + email);
    }
    
    @ParameterizedTest
    @EnumSource(Month.class)
    void shouldHandleAllMonths(Month month) {
        DateCalculator calculator = new DateCalculator();
        int daysInMonth = calculator.getDaysInMonth(month, 2024);
        
        assertTrue(daysInMonth >= 28 && daysInMonth <= 31,
            "Month " + month + " should have between 28-31 days");
    }
    
    @ParameterizedTest
    @EnumSource(value = DayOfWeek.class, names = {"SATURDAY", "SUNDAY"})
    void shouldIdentifyWeekends(DayOfWeek day) {
        WeekendChecker checker = new WeekendChecker();
        assertTrue(checker.isWeekend(day), 
            day + " should be identified as weekend");
    }
    
    @ParameterizedTest
    @EnumSource(value = DayOfWeek.class, mode = EnumSource.Mode.EXCLUDE, 
                names = {"SATURDAY", "SUNDAY"})
    void shouldIdentifyWeekdays(DayOfWeek day) {
        WeekendChecker checker = new WeekendChecker();
        assertFalse(checker.isWeekend(day), 
            day + " should be identified as weekday");
    }
    
    @ParameterizedTest
    @CsvFileSource(resources = "/test-data/user-data.csv", numLinesToSkip = 1)
    void shouldProcessUserData(String name, String email, int age, String expectedStatus) {
        User user = new User(name, email, age);
        UserProcessor processor = new UserProcessor();
        
        String actualStatus = processor.determineStatus(user);
        assertEquals(expectedStatus, actualStatus,
            String.format("User %s should have status %s", name, expectedStatus));
    }
    
    @ParameterizedTest
    @MethodSource("provideMathOperations")
    void shouldPerformMathOperations(int a, int b, String operation, double expected) {
        Calculator calculator = new Calculator();
        double result = switch (operation) {
            case "add" -> calculator.add(a, b);
            case "subtract" -> calculator.subtract(a, b);
            case "multiply" -> calculator.multiply(a, b);
            case "divide" -> calculator.divide(a, b);
            default -> throw new IllegalArgumentException("Unknown operation: " + operation);
        };
        
        assertEquals(expected, result, 0.001,
            String.format("%d %s %d should equal %.2f", a, operation, b, expected));
    }
    
    private static Stream<Arguments> provideMathOperations() {
        return Stream.of(
            Arguments.of(10, 5, "add", 15.0),
            Arguments.of(10, 5, "subtract", 5.0),
            Arguments.of(10, 5, "multiply", 50.0),
            Arguments.of(10, 5, "divide", 2.0),
            Arguments.of(20, 4, "add", 24.0),
            Arguments.of(20, 4, "subtract", 16.0),
            Arguments.of(20, 4, "multiply", 80.0),
            Arguments.of(20, 4, "divide", 5.0)
        );
    }
    
    @ParameterizedTest
    @ArgumentsSource(UserArgumentsProvider.class)
    void shouldValidateUsers(User user, boolean expectedValid) {
        UserValidator validator = new UserValidator();
        boolean isValid = validator.validate(user);
        
        assertEquals(expectedValid, isValid,
            String.format("User validation should return %s for user: %s", 
                expectedValid, user));
    }
    
    // Custom Arguments Provider
    static class UserArgumentsProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                Arguments.of(new User("John", "john@test.com", 25), true),
                Arguments.of(new User("", "invalid", -1), false),
                Arguments.of(new User("Jane", "jane@valid.com", 30), true),
                Arguments.of(new User(null, null, 0), false),
                Arguments.of(new User("Bob", "bob@example.org", 18), true)
            );
        }
    }
}

