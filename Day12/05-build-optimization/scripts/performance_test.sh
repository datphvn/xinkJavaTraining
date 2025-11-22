#!/bin/bash

echo "🚀 Build Performance Testing"
echo "=========================="

# Clean builds performance
echo "Testing clean build performance..."
time ./gradlew clean build --no-build-cache

# Cached builds performance  
echo "Testing cached build performance..."
time ./gradlew clean build

# Incremental builds performance
echo "Testing incremental build performance..."
touch modules/core/src/main/java/com/company/advanced/core/MathUtils.java
time ./gradlew build

# Parallel vs sequential comparison
echo "Testing parallel vs sequential builds..."
echo "Parallel build:"
time ./gradlew clean build --parallel --max-workers=4

echo "Sequential build:"
time ./gradlew clean build --no-parallel --max-workers=1

# Build scan analysis
echo "Generating build scan for analysis..."
./gradlew build --scan

