package com.xink.training.day13.exercise1;

import com.xink.training.day13.model.Address;
import com.xink.training.day13.model.User;
import com.xink.training.day13.service.Calculator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Advanced Assertions Test Suite")
class AdvancedAssertionsTest {
    
    @Test
    @DisplayName("Should demonstrate basic assertions")
    void basicAssertions() {
        // Boolean assertions
        assertTrue(2 + 2 == 4, "Basic math should work");
        assertFalse("hello".isEmpty(), "Non-empty string should not be empty");
        
        // Null assertions
        String notNull = "hello";
        String nullValue = null;
        assertNotNull(notNull, "String should not be null");
        assertNull(nullValue, "Value should be null");
        
        // Equality assertions
        assertEquals(4, 2 + 2, "Addition should work correctly");
        assertNotEquals(5, 2 + 2, "Addition should not equal wrong value");
        
        // Same/not same assertions (reference equality)
        String str1 = new String("hello");
        String str2 = new String("hello");
        String str3 = str1;
        
        assertNotSame(str1, str2, "Different string objects should not be same");
        assertSame(str1, str3, "Same string reference should be same");
        
        // Array assertions
        int[] expected = {1, 2, 3, 4, 5};
        int[] actual = {1, 2, 3, 4, 5};
        assertArrayEquals(expected, actual, "Arrays should be equal");
    }
    
    @Test
    @DisplayName("Should demonstrate collection assertions")
    void collectionAssertions() {
        List<String> fruits = Arrays.asList("apple", "banana", "cherry");
        
        // Using JUnit assertions
        assertEquals(3, fruits.size(), "List should have 3 elements");
        assertTrue(fruits.contains("banana"), "List should contain banana");
        
        // Using AssertJ for more fluent assertions
        assertThat(fruits)
            .hasSize(3)
            .contains("apple", "cherry")
            .doesNotContain("orange")
            .startsWith("apple")
            .endsWith("cherry")
            .containsExactly("apple", "banana", "cherry")
            .containsExactlyInAnyOrder("cherry", "apple", "banana");
        
        // Map assertions
        Map<String, Integer> scores = Map.of(
            "Alice", 95,
            "Bob", 87,
            "Charlie", 92
        );
        
        assertThat(scores)
            .hasSize(3)
            .containsKey("Alice")
            .containsValue(95)
            .containsEntry("Bob", 87)
            .doesNotContainKey("David");
    }
    
    @Test
    @DisplayName("Should demonstrate object assertions")
    void objectAssertions() {
        User user = new User("John", "john@example.com", 30);
        
        // Field-by-field assertions
        assertThat(user)
            .extracting(User::getName)
            .isEqualTo("John");
        
        assertThat(user)
            .extracting(User::getName, User::getEmail, User::getAge)
            .containsExactly("John", "john@example.com", 30);
        
        // Complex object assertions
        Address address = new Address("123 Main St", "Springfield", "IL", "62701");
        user.setAddress(address);
        
        assertThat(user)
            .extracting("address.city")
            .isEqualTo("Springfield");
        
        // Custom assertions
        assertThat(user).satisfies(u -> {
            assertThat(u.getName()).isNotEmpty();
            assertThat(u.getEmail()).contains("@");
            assertThat(u.getAge()).isBetween(18, 100);
        });
    }
    
    @Test
    @DisplayName("Should demonstrate exception assertions")
    void exceptionAssertions() {
        Calculator calculator = new Calculator();
        
        // Assert exception is thrown
        Exception exception = assertThrows(
            ArithmeticException.class,
            () -> calculator.divide(10, 0),
            "Division by zero should throw ArithmeticException"
        );
        
        // Assert exception message
        assertEquals("Cannot divide by zero", exception.getMessage());
        
        // Assert no exception is thrown
        assertDoesNotThrow(
            () -> calculator.divide(10, 2),
            "Valid division should not throw exception"
        );
        
        // Multiple exception scenarios
        assertAll("Exception scenarios",
            () -> assertThrows(IllegalArgumentException.class,
                () -> calculator.sqrt(-1)),
            () -> assertThrows(ArithmeticException.class,
                () -> calculator.divide(1, 0)),
            () -> assertDoesNotThrow(
                () -> calculator.add(1, 2))
        );
        
        // Using AssertJ for exception assertions
        assertThatThrownBy(() -> calculator.divide(10, 0))
            .isInstanceOf(ArithmeticException.class)
            .hasMessage("Cannot divide by zero")
            .hasNoCause();
        
        assertThatCode(() -> calculator.add(1, 2))
            .doesNotThrowAnyException();
    }
    
    @Test
    @DisplayName("Should demonstrate grouped assertions")
    void groupedAssertions() {
        User user = new User("Alice", "alice@example.com", 25);
        user.setCreatedAt(java.time.LocalDateTime.now());
        
        // All assertions executed even if some fail
        assertAll("User validation",
            () -> assertEquals("Alice", user.getName(), "Name should match"),
            () -> assertTrue(user.getEmail().contains("@"), "Email should be valid"),
            () -> assertTrue(user.getAge() > 0, "Age should be positive"),
            () -> assertNotNull(user.getCreatedAt(), "Created date should be set")
        );
        
        // Nested grouped assertions
        assertAll("User details",
            () -> assertAll("Basic info",
                () -> assertNotNull(user.getName()),
                () -> assertNotNull(user.getEmail())
            ),
            () -> assertAll("Age validation",
                () -> assertTrue(user.getAge() >= 18, "Should be adult"),
                () -> assertTrue(user.getAge() <= 120, "Should be reasonable age")
            )
        );
    }
    
    @Test
    @DisplayName("Should demonstrate timeout assertions")
    void timeoutAssertions() {
        // Assert operation completes within timeout
        assertTimeout(Duration.ofMillis(100), () -> {
            // Simulate fast operation
            Thread.sleep(50);
        });
        
        // Assert operation completes within timeout (preemptively)
        assertTimeoutPreemptively(Duration.ofMillis(100), () -> {
            // This will be interrupted if it takes too long
            performQuickOperation();
        });
        
        // Timeout with return value
        String result = assertTimeout(Duration.ofSeconds(1), () -> {
            return "operation result";
        });
        assertEquals("operation result", result);
        
        // Using AssertJ for timeout
        assertThatCode(() -> performQuickOperation())
            .doesNotThrowAnyException();
    }
    
    private String performQuickOperation() {
        // Simulate quick operation
        return "done";
    }
}

