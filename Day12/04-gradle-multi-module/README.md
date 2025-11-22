# Gradle Multi-Module Project

This project demonstrates a comprehensive multi-module Gradle setup with:
- Root project configuration
- Subproject configurations
- Dependency management
- Test aggregation
- Code coverage aggregation

## Module Structure

- **training-common**: Common utilities and shared code
- **training-domain**: Domain models and business logic
- **training-repository**: Data access layer
- **training-service**: Business service layer
- **training-web**: Web layer and REST APIs
- **training-integration-tests**: Integration tests for the entire application

## Build Commands

```bash
# Build all modules
./gradlew build

# Build specific module
./gradlew :training-web:build

# Run all tests
./gradlew testAll

# Generate aggregated coverage report
./gradlew jacocoAggregateReport

# Show dependency tree
./gradlew dependencies

# Show build environment
./gradlew buildEnvironment
```

## Tasks

- **testAll**: Runs all tests in all modules
- **jacocoAggregateReport**: Generates aggregated code coverage report

