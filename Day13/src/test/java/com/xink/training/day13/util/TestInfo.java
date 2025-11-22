package com.xink.training.day13.util;

import java.util.Optional;

public class TestInfo {
    private static ThreadLocal<String> currentTestMethod = new ThreadLocal<>();
    
    public static void setCurrentTestMethod(String methodName) {
        currentTestMethod.set(methodName);
    }
    
    public static Optional<String> getCurrentTestMethod() {
        return Optional.ofNullable(currentTestMethod.get());
    }
    
    public static void clear() {
        currentTestMethod.remove();
    }
}

