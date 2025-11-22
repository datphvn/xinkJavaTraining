package com.xink.training.taskmanagement.analytics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductivityTrend {
    private LocalDate date;
    private Long taskCount;
}

