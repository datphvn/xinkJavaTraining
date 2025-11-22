package com.xink.training.day13.exercise1;

import com.xink.training.day13.service.Calculator;
import com.xink.training.day13.util.DatabaseTestHelper;
import com.xink.training.day13.util.TestDataBuilder;
import com.xink.training.day13.util.TestDataManager;
import com.xink.training.day13.util.TestInfo;
import com.xink.training.day13.util.TestReportManager;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Calculator Test Suite")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CalculatorTest {
    
    private Calculator calculator;
    private static TestDataBuilder testDataBuilder;
    
    // Class-level setup (once per test class)
    @BeforeAll
    static void initializeTestSuite() {
        System.out.println("🚀 Starting Calculator Test Suite");
        testDataBuilder = new TestDataBuilder();
        // Setup expensive resources: database connections, external services
        DatabaseTestHelper.initializeTestDatabase();
        TestReportManager.startReport("CalculatorTest");
    }
    
    // Method-level setup (before each test)
    @BeforeEach
    void setUp(org.junit.jupiter.api.TestInfo testInfo) {
        calculator = new Calculator();
        System.out.println("📝 Setting up test: " + testInfo.getDisplayName());
        // Reset state, clear caches, prepare test data
        TestDataManager.resetState();
        TestInfo.setCurrentTestMethod(testInfo.getTestMethod().map(m -> m.getName()).orElse("Unknown"));
    }
    
    // Method-level cleanup (after each test)
    @AfterEach
    void tearDown(org.junit.jupiter.api.TestInfo testInfo) {
        System.out.println("✅ Completed test: " + testInfo.getDisplayName());
        // Clean up resources, validate invariants
        TestDataManager.cleanup();
        calculator = null;
        TestInfo.clear();
    }
    
    // Class-level cleanup (once after all tests)
    @AfterAll
    static void cleanupTestSuite() {
        System.out.println("🏁 Calculator Test Suite completed");
        // Cleanup expensive resources
        DatabaseTestHelper.cleanupTestDatabase();
        TestReportManager.generateReport();
    }
    
    @Test
    @DisplayName("Should add two numbers correctly")
    void shouldAddTwoNumbers() {
        int result = calculator.add(2, 3);
        assertEquals(5, result);
    }
    
    @Test
    @DisplayName("Should subtract two numbers correctly")
    void shouldSubtractTwoNumbers() {
        int result = calculator.subtract(10, 4);
        assertEquals(6, result);
    }
    
    @Test
    @DisplayName("Should multiply two numbers correctly")
    void shouldMultiplyTwoNumbers() {
        int result = calculator.multiply(5, 6);
        assertEquals(30, result);
    }
    
    @Test
    @DisplayName("Should divide two numbers correctly")
    void shouldDivideTwoNumbers() {
        double result = calculator.divide(10, 2);
        assertEquals(5.0, result, 0.001);
    }
    
    @Test
    @DisplayName("Should throw exception when dividing by zero")
    void shouldThrowExceptionWhenDividingByZero() {
        assertThrows(ArithmeticException.class, () -> calculator.divide(10, 0));
    }
}

