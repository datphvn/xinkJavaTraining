package com.frp.backpressure;

import com.frp.core.EventSubscriber;

import java.util.concurrent.atomic.AtomicLong;

// 2. Backpressure handling
// Mô phỏng Backpressure bằng cách quản lý số lượng yêu cầu (requests)
public class SubscriptionManager<T> {
    private final EventSubscriber<T> subscriber;
    // Sử dụng AtomicLong để đếm số lượng event mà Subscriber đã yêu cầu
    private final AtomicLong requested = new AtomicLong(0);

    public SubscriptionManager(EventSubscriber<T> subscriber) {
        this.subscriber = subscriber;
    }

    // Subscriber yêu cầu thêm n events
    public void request(long n) {
        if (n <= 0) {
            subscriber.onError(new IllegalArgumentException("Request must be positive."));
            return;
        }
        // Tăng số lượng yêu cầu lên, đảm bảo an toàn luồng
        requested.addAndGet(n);
        System.out.printf("[Backpressure] Subscriber requested %d more items.%n", n);
    }

    // Publisher kiểm tra và phát hành event
    public boolean tryEmit(T event) {
        // Kiểm tra xem có đủ yêu cầu (requests) để phát hành không
        if (requested.get() > 0) {
            // Giảm số lượng yêu cầu
            long remaining = requested.decrementAndGet();

            // Phát hành event
            subscriber.onNext(event);

            // Mô phỏng giám sát hiệu suất: Nếu yêu cầu giảm xuống 0, tốc độ xử lý có thể quá chậm
            if (remaining == 0) {
                System.out.println("[Backpressure] Buffer depleted. Waiting for next request...");
            }
            return true;
        }
        // Không đủ yêu cầu (Backpressure activated)
        return false;
    }

    public EventSubscriber<T> getSubscriber() {
        return subscriber;
    }
}