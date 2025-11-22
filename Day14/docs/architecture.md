# Architecture Documentation

## System Architecture

### Multi-Module Structure

The project follows a **layered architecture** with clear separation of concerns:

```
┌─────────────────────────────────────────┐
│         task-api (Web Layer)            │
│  - REST Controllers                     │
│  - Request/Response DTOs                │
└─────────────────┬───────────────────────┘
                  │
┌─────────────────▼───────────────────────┐
│      task-service (Service Layer)        │
│  - Business Logic                        │
│  - Transaction Management                 │
│  - Analytics Services                    │
└─────────────────┬───────────────────────┘
                  │
┌─────────────────▼───────────────────────┐
│       task-core (Domain Layer)          │
│  - Domain Models                         │
│  - Entities                              │
│  - Repositories                         │
└─────────────────────────────────────────┘
```

### Domain Model

#### TaskStatus (Sealed Interface)
Uses modern Java sealed classes to ensure type safety:

```java
public sealed interface TaskStatus 
    permits Todo, InProgress, Review, Done, Cancelled
```

Benefits:
- Compile-time type checking
- Exhaustive pattern matching
- Clear state machine

#### Task Entity
Rich domain model with business logic:
- Status transitions with validation
- Time tracking integration
- Dependency management
- Subtask relationships

### Service Layer

#### Functional Programming
Extensive use of:
- **Stream API** for data processing
- **Lambda expressions** for predicates and functions
- **Optional** for null-safe operations
- **Method references** for cleaner code

#### Parallel Processing
Bulk operations use `CompletableFuture` and parallel streams for performance.

### Analytics Engine

Advanced analytics using:
- Complex Stream operations
- Statistical analysis
- Predictive insights
- Workload distribution analysis

## Design Patterns

1. **Repository Pattern** - Data access abstraction
2. **Service Layer Pattern** - Business logic encapsulation
3. **Builder Pattern** - Fluent object creation
4. **Strategy Pattern** - Status transition logic
5. **Observer Pattern** - Notification system

## Technology Choices

- **Java 17** - Modern language features
- **Spring Boot** - Rapid development
- **H2 Database** - Development simplicity
- **Maven** - Dependency management
- **JUnit 5** - Modern testing

