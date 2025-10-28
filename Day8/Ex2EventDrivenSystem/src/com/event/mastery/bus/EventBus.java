package com.event.mastery.bus;

import com.event.mastery.event.Event;
import com.event.mastery.handler.EventHandler;

import java.util.*;
import java.util.concurrent.*;

public class EventBus {
    // Registry: Ánh xạ loại sự kiện (String) tới danh sách các Handler
    private final Map<String, List<EventHandler<?>>> handlerRegistry = new ConcurrentHashMap<>();

    // Thread Pool cho xử lý Bất đồng bộ (Async)
    private final ExecutorService executor = Executors.newFixedThreadPool(4);

    // Persistence: Dùng List đơn giản để mô phỏng Event Sourcing/Persistence
    private final List<Event> eventStore = Collections.synchronizedList(new ArrayList<>());

    // --- 1. Registration và Deregistration (FP-Style) ---

    // Handler phải được đăng ký dựa trên loại Class của Event
    public <T extends Event> void register(Class<T> eventType, EventHandler<T> handler) {
        // Tên sự kiện (String) làm khóa routing
        String typeName = eventType.getSimpleName();

        // Sử dụng computeIfAbsent để thread-safe
        handlerRegistry.computeIfAbsent(typeName, k -> Collections.synchronizedList(new ArrayList<>()))
                .add(handler);

        System.out.printf("[BUS] Handler for %s registered.%n", typeName);
    }

    public <T extends Event> void deregister(Class<T> eventType, EventHandler<T> handler) {
        String typeName = eventType.getSimpleName();
        List<EventHandler<?>> handlers = handlerRegistry.get(typeName);
        if (handlers != null) {
            handlers.remove(handler);
            System.out.printf("[BUS] Handler for %s deregistered.%n", typeName);
        }
    }

    // --- 2. Event Publishing (Routing và Dispatch) ---

    // Phương thức publish chính
    public void publish(Event event) {
        // 1. Persistence (Event Sourcing)
        eventStore.add(event);

        // 2. Dispatch
        String typeName = event.getType();
        List<EventHandler<?>> handlers = handlerRegistry.get(typeName);

        if (handlers == null || handlers.isEmpty()) {
            System.out.printf("[BUS] No handlers for %s. Event ID: %s%n", typeName, event.getId());
            return;
        }

        System.out.printf("[BUS] Dispatching %s to %d handlers...%n", typeName, handlers.size());

        // 3. Asynchronous Processing
        for (EventHandler<?> handler : handlers) {
            // Sử dụng CompletableFuture để chạy bất đồng bộ
            CompletableFuture.runAsync(() -> {
                // Type casting an toàn trước khi gọi handle
                @SuppressWarnings("unchecked")
                EventHandler<Event> castedHandler = (EventHandler<Event>) handler;
                castedHandler.handle(event);
            }, executor);
        }
    }

    // --- 3. Event Replay Capabilities ---

    // Phát lại (replay) tất cả các sự kiện đã lưu trữ
    public void replayAll() {
        System.out.println("\n[BUS] BẮT ĐẦU REPLAY ALL EVENTS...");
        eventStore.forEach(this::publishSynchronous); // Phát lại đồng bộ để đảm bảo thứ tự
        System.out.println("[BUS] REPLAY HOÀN TẤT.");
    }

    // Phát hành đồng bộ (dùng cho replay)
    private void publishSynchronous(Event event) {
        String typeName = event.getType();
        List<EventHandler<?>> handlers = handlerRegistry.get(typeName);

        if (handlers != null) {
            handlers.stream()
                    .filter(Objects::nonNull)
                    .forEach(handler -> {
                        @SuppressWarnings("unchecked")
                        EventHandler<Event> castedHandler = (EventHandler<Event>) handler;
                        castedHandler.handle(event);
                    });
        }
    }

    public void shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException ie) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        System.out.println("[BUS] Event Bus Shut down.");
    }
}
