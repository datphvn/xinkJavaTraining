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
public class TaskSearchCriteria {
    private String searchText;
    private List<String> statuses;
    private List<String> priorities;
    private List<UUID> assigneeIds;
    private List<UUID> projectIds;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private List<String> labels;
    private String sortBy;
    private String sortDirection;
    private int offset = 0;
    private int limit = 50;
}

