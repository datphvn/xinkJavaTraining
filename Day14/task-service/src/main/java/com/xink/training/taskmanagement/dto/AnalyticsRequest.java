package com.xink.training.taskmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsRequest {
    private LocalDate fromDate;
    private LocalDate toDate;
    private List<UUID> projectIds;
    private List<UUID> userIds;
}

