package com.xink.training.taskmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTaskRequest {
    private String title;
    private String description;
    private String priority;
    private UUID creatorId;
    private UUID assigneeId;
    private UUID projectId;
    private LocalDateTime dueDate;
    private Long estimatedHours;
    private List<String> labels;
}

