package com.company.advanced.core;

public class MathUtils {
    
    public static double calculateCircleArea(double radius) {
        if (radius < 0) {
            throw new IllegalArgumentException("Radius cannot be negative");
        }
        return Math.PI * radius * radius;
    }
    
    public static boolean isPrime(int number) {
        if (number < 2) return false;
        for (int i = 2; i <= Math.sqrt(number); i++) {
            if (number % i == 0) return false;
        }
        return true;
    }
    
    public static long factorial(int n) {
        if (n < 0) throw new IllegalArgumentException("Factorial not defined for negative numbers");
        if (n <= 1) return 1;
        return n * factorial(n - 1);
    }
}

