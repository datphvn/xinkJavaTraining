# Quick Start Guide

## 🚀 Getting Started in 3 Steps

### Step 1: Build the Project
```bash
mvn clean install
```

### Step 2: Run the Application
```bash
cd task-api
mvn spring-boot:run
```

### Step 3: Test the API
```bash
# Create a task
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "title": "My First Task",
    "description": "Task description",
    "priority": "HIGH",
    "creatorId": "your-user-id"
  }'

# Get all tasks
curl http://localhost:8080/api/tasks

# Search tasks
curl "http://localhost:8080/api/tasks/search?searchText=First&priority=HIGH"
```

## 📊 Access H2 Console

1. Navigate to: http://localhost:8080/h2-console
2. JDBC URL: `jdbc:h2:mem:taskdb`
3. Username: `sa`
4. Password: (empty)

## 🧪 Run Tests

```bash
# Run all tests
mvn test

# Run specific module tests
cd task-core
mvn test
```

## 📁 Project Modules

- **task-core**: Domain models and repositories
- **task-service**: Business logic and services
- **task-api**: REST API controllers
- **task-analytics**: Analytics and reporting

## 🎯 Key Endpoints

- `POST /api/tasks` - Create task
- `GET /api/tasks/{id}` - Get task
- `GET /api/tasks/search` - Search tasks
- `POST /api/tasks/bulk-update` - Bulk update
- `POST /api/analytics/task-analytics` - Get analytics

## 💡 Tips

- Check `application.yml` for configuration
- See `docs/api.md` for full API documentation
- See `docs/architecture.md` for system design

