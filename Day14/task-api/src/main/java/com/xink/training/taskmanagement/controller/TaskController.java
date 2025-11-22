package com.xink.training.taskmanagement.controller;

import com.xink.training.taskmanagement.domain.Task;
import com.xink.training.taskmanagement.dto.*;
import com.xink.training.taskmanagement.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {
    
    private final TaskService taskService;
    
    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody CreateTaskRequest request) {
        Task task = taskService.createTask(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTask(@PathVariable UUID id) {
        return taskService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskService.findAll());
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<Task>> searchTasks(@ModelAttribute TaskSearchCriteria criteria) {
        return ResponseEntity.ok(taskService.searchTasks(criteria));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable UUID id, 
                                           @RequestBody CreateTaskRequest request) {
        try {
            Task task = taskService.updateTask(id, request);
            return ResponseEntity.ok(task);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable UUID id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/bulk-update")
    public ResponseEntity<CompletableFuture<BulkUpdateResult>> bulkUpdateTasks(
            @RequestBody BulkUpdateRequest request) {
        CompletableFuture<BulkUpdateResult> result = taskService.bulkUpdateTasks(request);
        return ResponseEntity.accepted().body(result);
    }
}

