#!/bin/bash

echo "🚀 Maven Build Performance Testing"
echo "================================="

# Clean builds performance
echo "Testing clean build performance..."
time mvn clean package -DskipTests

# With tests
echo "Testing build with tests..."
time mvn clean package

# Parallel builds
echo "Testing parallel builds..."
time mvn clean package -T 4

# Incremental builds
echo "Testing incremental builds..."
touch core/src/main/java/com/company/advanced/core/Calculator.java
time mvn package

# Profile-specific builds
echo "Testing profile builds..."
time mvn clean package -Pproduction -DskipTests

