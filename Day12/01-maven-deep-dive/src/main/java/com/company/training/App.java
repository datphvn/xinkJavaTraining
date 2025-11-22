package com.company.training;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main application class demonstrating Maven configuration
 */
public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);
    
    public static void main(String[] args) {
        logger.info("Java Training Project with Maven");
        logger.info("Version: 1.0.0-SNAPSHOT");
        
        Calculator calculator = new Calculator();
        logger.info("2 + 3 = {}", calculator.add(2, 3));
        logger.info("5 - 2 = {}", calculator.subtract(5, 2));
    }
}

