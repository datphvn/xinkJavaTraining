package com.xink.training.day13.util;

public class DatabaseTestHelper {
    private static boolean initialized = false;
    
    public static void initializeTestDatabase() {
        initialized = true;
        System.out.println("Database initialized for testing");
    }
    
    public static void cleanupTestDatabase() {
        initialized = false;
        System.out.println("Database cleaned up");
    }
    
    public static boolean isInitialized() {
        return initialized;
    }
}

