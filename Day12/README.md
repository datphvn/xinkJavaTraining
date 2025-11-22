# Day 12: Maven & Gradle Build Automation Mastery

This directory contains all exercises and projects for Day 12 training on Maven and Gradle Build Automation.

## Project Structure

### 1. Maven Deep Dive (`01-maven-deep-dive`)
Comprehensive Maven configuration with:
- Complete POM structure
- Advanced plugin configuration
- Code coverage with JaCoCo
- Integration test setup

### 2. Gradle Deep Dive (`02-gradle-deep-dive`)
Comprehensive Gradle configuration with:
- Complete build.gradle configuration
- Custom tasks
- Source sets configuration
- Build cache configuration

### 3. Maven Multi-Module (`03-maven-multi-module`)
Multi-module Maven project demonstrating:
- Parent POM with dependency management
- Multiple modules with proper dependencies
- Integration test configuration
- Code coverage aggregation

### 4. Gradle Multi-Module (`04-gradle-multi-module`)
Multi-module Gradle project demonstrating:
- Root project configuration
- Subproject configurations
- Test aggregation
- Code coverage aggregation

### 5. Build Optimization (`05-build-optimization`)
Build optimization examples with:
- Gradle optimization configuration
- Maven optimization configuration
- Performance testing scripts
- Cache analysis scripts

### 6. Advanced Maven Project (`06-advanced-maven-project`)
Advanced Maven project with best practices:
- Multi-module setup
- Profile management
- Static analysis
- Integration test configuration

### 7. Advanced Gradle Project (`07-advanced-gradle-project`)
Advanced Gradle project with Kotlin DSL:
- Multi-module setup
- Custom tasks
- Test aggregation
- Spring Boot integration

## Quick Start

Each project directory contains its own README with specific instructions. Navigate to any project directory and follow the instructions in its README.

## Common Commands

### Maven
```bash
# Clean and compile
mvn clean compile

# Run tests
mvn test

# Package application
mvn package

# Run with integration tests
mvn clean verify

# Generate code coverage
mvn jacoco:report

# View dependency tree
mvn dependency:tree
```

### Gradle
```bash
# Build project
./gradlew build

# Run tests
./gradlew test

# Clean build
./gradlew clean build

# Show dependency tree
./gradlew dependencies

# Show all tasks
./gradlew tasks --all
```

## Learning Objectives

After completing Day 12, you should be able to:
- Create comprehensive Maven and Gradle configurations
- Set up multi-module projects
- Configure build optimization
- Implement code coverage and testing
- Use profiles and custom tasks
- Optimize build performance

## Resources

- [Maven Documentation](https://maven.apache.org/guides/)
- [Gradle User Manual](https://docs.gradle.org/current/userguide/userguide.html)
- [Maven Central Repository](https://search.maven.org/)

