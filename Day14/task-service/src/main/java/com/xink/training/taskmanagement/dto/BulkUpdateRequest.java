package com.xink.training.taskmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BulkUpdateRequest {
    private List<UUID> taskIds;
    private String newPriority;
    private UUID newAssigneeId;
    private String newStatus;
    private List<String> labelsToAdd;
    private List<String> labelsToRemove;
}

