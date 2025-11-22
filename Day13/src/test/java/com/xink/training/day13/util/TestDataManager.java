package com.xink.training.day13.util;

public class TestDataManager {
    private static Object state;
    
    public static void resetState() {
        state = null;
    }
    
    public static void cleanup() {
        state = null;
    }
    
    public static Object getState() {
        return state;
    }
    
    public static void setState(Object state) {
        TestDataManager.state = state;
    }
}

