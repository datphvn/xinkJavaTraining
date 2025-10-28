package com.event.mastery.handler;

import com.event.mastery.event.UserCreatedEvent;

// Handler chỉ cho UserCreatedEvent
public class EmailHandler implements EventHandler<UserCreatedEvent> {
    @Override
    public void handle(UserCreatedEvent event) {
        System.out.printf("[EMAIL] Sending Welcome Email to %s | Thread: %s%n",
                event.getEmail(), Thread.currentThread().getName());
    }
}
