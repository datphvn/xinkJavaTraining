# API Documentation

## Task Management API

### Create Task
```http
POST /api/tasks
Content-Type: application/json

{
  "title": "Task Title",
  "description": "Task Description",
  "priority": "HIGH",
  "creatorId": "uuid",
  "assigneeId": "uuid",
  "projectId": "uuid",
  "dueDate": "2024-12-31T23:59:59",
  "estimatedHours": 8,
  "labels": ["urgent", "backend"]
}
```

### Get Task
```http
GET /api/tasks/{id}
```

### Get All Tasks
```http
GET /api/tasks
```

### Search Tasks
```http
GET /api/tasks/search?searchText=keyword&priority=HIGH&status=IN_PROGRESS
```

### Update Task
```http
PUT /api/tasks/{id}
Content-Type: application/json

{
  "title": "Updated Title",
  "description": "Updated Description"
}
```

### Delete Task
```http
DELETE /api/tasks/{id}
```

### Bulk Update Tasks
```http
POST /api/tasks/bulk-update
Content-Type: application/json

{
  "taskIds": ["uuid1", "uuid2"],
  "newPriority": "URGENT",
  "newStatus": "IN_PROGRESS",
  "labelsToAdd": ["label1"],
  "labelsToRemove": ["label2"]
}
```

## Analytics API

### Get Task Analytics
```http
POST /api/analytics/task-analytics
Content-Type: application/json

{
  "fromDate": "2024-01-01",
  "toDate": "2024-12-31",
  "projectIds": ["uuid"],
  "userIds": ["uuid"]
}
```

### Get Productivity Report
```http
POST /api/analytics/productivity-report
Content-Type: application/json

{
  "fromDate": "2024-01-01",
  "toDate": "2024-12-31"
}
```

### Get Performance Metrics
```http
POST /api/analytics/performance-metrics
Content-Type: application/json

{
  "fromDate": "2024-01-01",
  "toDate": "2024-12-31"
}
```

### Get Predictive Insights
```http
POST /api/analytics/predictive-insights
Content-Type: application/json

{
  "fromDate": "2024-01-01",
  "toDate": "2024-12-31"
}
```

