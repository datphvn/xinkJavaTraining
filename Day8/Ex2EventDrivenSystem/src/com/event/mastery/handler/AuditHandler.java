package com.event.mastery.handler;

import com.event.mastery.event.Event;

// Handler cho mọi sự kiện (Generic Handler)
public class AuditHandler implements EventHandler<Event> {
    @Override
    public void handle(Event event) {
        System.out.printf("[AUDIT] Logged event: %s | Thread: %s%n",
                event.getType(), Thread.currentThread().getName());

        // Mô phỏng lỗi ngẫu nhiên để kiểm tra .safe()
        if (Math.random() < 0.1) {
            throw new RuntimeException("Simulated Audit Failure!");
        }
    }
}
