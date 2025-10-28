package com.event.mastery.event;

import java.math.BigDecimal;

// Sự kiện khi một đơn hàng được đặt
public class OrderPlacedEvent extends Event {
    private final String orderId;
    private final BigDecimal totalAmount;

    public OrderPlacedEvent(String source, String orderId, BigDecimal totalAmount) {
        super(source);
        this.orderId = orderId;
        this.totalAmount = totalAmount;
    }

    public String getOrderId() { return orderId; }
    public BigDecimal getTotalAmount() { return totalAmount; }

    @Override
    public String toString() {
        return super.toString() + String.format(" | Order ID: %s, Amount: %.2f", orderId, totalAmount);
    }
}
