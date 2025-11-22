package com.xink.training.day13.exercise1;

import org.junit.jupiter.api.*;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Test Information Injection Examples")
class TestInfoInjectionTest {
    
    @Test
    @DisplayName("Should inject test information correctly")
    @Tag("unit")
    @Tag("fast")
    void testWithInjectedInfo(TestInfo testInfo, TestReporter testReporter) {
        // TestInfo provides metadata about current test
        String displayName = testInfo.getDisplayName();
        String methodName = testInfo.getTestMethod().get().getName();
        Set<String> tags = testInfo.getTags();
        Class<?> testClass = testInfo.getTestClass().get();
        
        testReporter.publishEntry("displayName", displayName);
        testReporter.publishEntry("methodName", methodName);
        testReporter.publishEntry("tags", tags.toString());
        testReporter.publishEntry("testClass", testClass.getSimpleName());
        
        // Assertions về test metadata
        assertThat(displayName).isEqualTo("Should inject test information correctly");
        assertThat(tags).contains("unit", "fast");
        assertThat(testClass).isEqualTo(TestInfoInjectionTest.class);
    }
    
    @RepeatedTest(value = 3, name = "Iteration {currentRepetition} of {totalRepetitions}")
    void repeatedTestWithInfo(RepetitionInfo repetitionInfo, TestReporter testReporter) {
        int currentRepetition = repetitionInfo.getCurrentRepetition();
        int totalRepetitions = repetitionInfo.getTotalRepetitions();
        
        testReporter.publishEntry("repetition", 
            String.format("%d/%d", currentRepetition, totalRepetitions));
        
        // Different logic based on repetition
        if (currentRepetition == 1) {
            // First iteration setup
            System.out.println("First iteration - initializing data");
        } else if (currentRepetition == totalRepetitions) {
            // Last iteration cleanup
            System.out.println("Last iteration - finalizing test");
        }
        
        assertTrue(currentRepetition <= totalRepetitions);
    }
}

