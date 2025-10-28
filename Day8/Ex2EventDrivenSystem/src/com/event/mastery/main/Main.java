package com.event.mastery.main;

import com.event.mastery.bus.EventBus;
import com.event.mastery.event.Event;
import com.event.mastery.event.OrderPlacedEvent;
import com.event.mastery.event.UserCreatedEvent;
import com.event.mastery.handler.AuditHandler;
import com.event.mastery.handler.EmailHandler;
import com.event.mastery.handler.EventHandler;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

public class Main {
    public static void main(String[] args) throws IOException {

        // --- Bắt đầu Exercise 2: Event-Driven System ---

        System.out.println("=================================================");
        System.out.println("             EXERCISE 2: EVENT BUS DEMO          ");
        System.out.println("   Minh họa Composition, Filtering và Async/Replay   ");
        System.out.println("=================================================");

        EventBus bus = new EventBus();

        // 1. Tạo các Handler nghiệp vụ
        EmailHandler emailHandler = new EmailHandler();
        AuditHandler auditHandler = new AuditHandler();

        // Handler cho OrderPlacedEvent (dùng lambda)
        EventHandler<OrderPlacedEvent> inventoryHandler = event -> {
            System.out.printf("[INVTY] Deducting stock for Order %s | Thread: %s%n",
                    event.getOrderId(), Thread.currentThread().getName());
        };

        // 2. Composition và Filtering (Sử dụng Lập trình Hàm)

        // Tạo một Safe Audit Handler chung (kiểu Event)
        EventHandler<Event> safeAudit = auditHandler;

        // FIX 1: Tạo các Lambda Wrapper định kiểu rõ ràng để giải quyết lỗi Generic
        // Wrapper này đảm bảo type T=UserCreatedEvent, sau đó gọi Handler kiểu Event
        EventHandler<UserCreatedEvent> auditWrapperForUsers = e -> safeAudit.handle(e);
        // Wrapper này đảm bảo type T=OrderPlacedEvent, sau đó gọi Handler kiểu Event
        EventHandler<OrderPlacedEvent> auditWrapperForOrders = e -> safeAudit.handle(e);

        // Điều kiện lọc: User ID bắt đầu bằng 'A'
        Predicate<UserCreatedEvent> isUserA = event -> event.getUserId().startsWith("A");

        // Chuỗi Handler cho UserCreatedEvent:
        EventHandler<UserCreatedEvent> userHandlerChain =
                emailHandler.withFilter(isUserA) // Chỉ gửi email cho user 'A'
                        .andThen(auditWrapperForUsers); // Sử dụng Wrapper đã định kiểu

        // Điều kiện lọc: Đơn hàng lớn (> 1000)
        Predicate<OrderPlacedEvent> isLargeOrder = event -> event.getTotalAmount().compareTo(new BigDecimal("1000")) > 0;

        // Chuỗi Handler cho OrderPlacedEvent:
        EventHandler<OrderPlacedEvent> largeOrderChain =
                inventoryHandler.withFilter(isLargeOrder) // Chỉ trừ kho cho đơn hàng lớn
                        .andThen(auditWrapperForOrders); // Sử dụng Wrapper đã định kiểu


        // 3. Đăng ký Handler vào Bus
        bus.register(UserCreatedEvent.class, userHandlerChain);
        bus.register(OrderPlacedEvent.class, largeOrderChain);

        // FIX LỖI INCOMPATIBLE TYPES TẠI ĐÂY:
        // Đăng ký Audit Handler chung (safeAudit) vào Event.class bằng cách truyền biến đã được định kiểu rõ ràng.
        bus.register(Event.class, safeAudit);


        // 4. Phát hành Sự kiện (Publishing - ASYNC)
        System.out.println("\n--- 4. BẮT ĐẦU PHÁT HÀNH SỰ KIỆN ASYNC (Kết quả có thể không theo thứ tự) ---");
        // Event 1: User A -> Gửi Email Ưu tiên + Audit
        bus.publish(new UserCreatedEvent("UI_Service", "A456", "alice@criticalcorp.com"));
        // Event 2: User B -> Chỉ Audit (không gửi email do bị lọc)
        bus.publish(new UserCreatedEvent("API_Gateway", "B789", "bob@example.com"));
        // Event 3: Đơn nhỏ -> Chỉ Audit (không trừ kho do bị lọc)
        bus.publish(new OrderPlacedEvent("Checkout", "ORD-101", new BigDecimal("550.00")));
        // Event 4: Đơn lớn -> Inventory + Audit
        bus.publish(new OrderPlacedEvent("Checkout", "ORD-102", new BigDecimal("1200.00")));

        // Chờ các luồng bất đồng bộ hoàn thành
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // 5. Event Replay
        System.out.println("\n--- 5. BẮT ĐẦU EVENT REPLAY (Đồng bộ, đảm bảo thứ tự) ---");
        bus.replayAll();


        // Dọn dẹp
        bus.shutdown();
    }
}
