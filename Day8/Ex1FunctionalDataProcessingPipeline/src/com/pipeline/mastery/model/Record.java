package com.pipeline.mastery.model;

import java.math.BigDecimal;
import java.util.Objects;

// Sử dụng Record để có sẵn Immutability (nguyên tắc FP)
public record Record(
        int id,
        String name,
        BigDecimal value,
        String status
) {
    public Record {
        Objects.requireNonNull(name, "Name cannot be null");
        Objects.requireNonNull(value, "Value cannot be null");
        Objects.requireNonNull(status, "Status cannot be null");
    }

    // Phương thức copy cho các bước biến đổi (Immutability)
    public Record withValue(BigDecimal newValue) {
        return new Record(this.id, this.name, newValue, this.status);
    }

    public Record withStatus(String newStatus) {
        return new Record(this.id, this.name, this.value, newStatus);
    }
}
