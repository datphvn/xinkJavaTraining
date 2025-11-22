# Advanced Gradle Project

This project demonstrates advanced Gradle configuration with Kotlin DSL including:
- Multi-module setup with Kotlin DSL
- Custom tasks
- Test aggregation
- Code coverage aggregation
- Spring Boot integration

## Module Structure

- **core**: Core utilities and shared functionality
- **api**: API layer definitions
- **web**: Web application
- **integration-tests**: Integration tests

## Build Commands

```bash
# Build all modules
./gradlew build

# Run all tests
./gradlew testAll

# Generate aggregated coverage report
./gradlew jacocoAggregateReport

# Build specific module
./gradlew :core:build

# Show all tasks
./gradlew tasks --all
```

## Custom Tasks

- **testAll**: Runs all tests in all modules
- **jacocoAggregateReport**: Generates aggregated code coverage report

