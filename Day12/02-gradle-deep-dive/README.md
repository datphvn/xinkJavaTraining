# Gradle Deep Dive Project

This project demonstrates comprehensive Gradle configuration with:
- Complete build.gradle configuration
- Custom tasks
- Source sets configuration
- Test configuration
- Integration test setup
- Build cache configuration

## Build Commands

```bash
# Build project
./gradlew build

# Run unit tests
./gradlew unitTest

# Run integration tests
./gradlew integrationTest

# Generate test data
./gradlew generateTestData

# Run database migrations
./gradlew dbMigrate

# Run performance tests
./gradlew performanceTest

# Show dependency tree
./gradlew dependencies

# Show task dependencies
./gradlew taskTree
```

## Custom Tasks

- **unitTest**: Runs unit tests only
- **integrationTest**: Runs integration tests
- **dbMigrate**: Runs database migrations
- **generateTestData**: Generates test data for development
- **performanceTest**: Runs performance tests

