package com.xink.training.taskmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BulkUpdateResult {
    private int totalRequested;
    private int successful;
    private int failed;
    private List<TaskUpdateResult> results;
}

