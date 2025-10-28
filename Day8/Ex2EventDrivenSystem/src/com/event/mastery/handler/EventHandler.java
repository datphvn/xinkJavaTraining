package com.event.mastery.handler;

import com.event.mastery.event.Event;
import com.event.mastery.event.UserCreatedEvent;

import java.util.function.Predicate;

// 1. Event Handler Interface (Functional Interface)
@FunctionalInterface
public interface EventHandler<T extends Event> {
    void handle(T event);

    // 2. Handler Composition: Chain handlers (andThen)
    default EventHandler<T> andThen(EventHandler<T> next) {
        return event -> {
            this.handle(event);
            next.handle(event);
        };
    }

    // 3. Filtering: Apply handler only if predicate passes (withFilter)
    default EventHandler<T> withFilter(Predicate<T> filter) {
        return event -> {
            if (filter.test(event)) {
                this.handle(event);
            }
        };
    }

    // 4. Error Handling: Gracefully wrap the handler to catch runtime exceptions
    default EventHandler<UserCreatedEvent> safe() {
        return event -> {
            try {
                this.handle(event);
            } catch (Exception e) {
                System.err.printf("[DLQ] Handler failed for %s: %s%n", event.getType(), e.getMessage());
                // In a real system, the DLQ would be called here: DeadLetterQueue.send(event, e);
            }
        };
    }
}
