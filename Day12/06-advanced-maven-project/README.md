# Advanced Maven Project

This project demonstrates advanced Maven configuration with best practices including:
- Comprehensive POM structure
- Multi-module setup
- Profile management
- Code coverage with JaCoCo
- Integration test configuration
- Static analysis with SpotBugs

## Module Structure

- **core**: Core utilities and shared functionality
- **api**: API layer definitions
- **web**: Web application

## Build Commands

```bash
# Development build (default profile)
mvn clean compile test -Pdevelopment

# Production build with static analysis
mvn clean package -Pproduction

# Run integration tests
mvn clean verify -Pintegration-test

# Generate code coverage report
mvn jacoco:report

# View dependency tree
mvn dependency:tree
```

## Profiles

- **development**: Default profile for development (skips integration tests)
- **production**: Production profile with static analysis
- **integration-test**: Enables integration test execution

