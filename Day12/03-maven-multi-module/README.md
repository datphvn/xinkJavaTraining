# Maven Multi-Module Project

This project demonstrates a comprehensive multi-module Maven setup with:
- Parent POM with dependency management
- Multiple modules with proper dependencies
- Integration test configuration
- Code coverage aggregation
- Profile-based builds

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
mvn clean install

# Build specific module
mvn clean install -pl training-web

# Run tests for all modules
mvn test

# Run integration tests
mvn clean verify -Pintegration-test

# Generate aggregated coverage report
mvn clean verify -Pcoverage-aggregate

# Show dependency tree
mvn dependency:tree

# Show effective POM
mvn help:effective-pom
```

## Profiles

- **integration-test**: Enables integration test execution
- **coverage-aggregate**: Generates aggregated code coverage reports

