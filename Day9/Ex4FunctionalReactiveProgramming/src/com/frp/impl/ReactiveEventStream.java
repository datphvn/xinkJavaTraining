package com.frp.impl;

import com.frp.backpressure.SubscriptionManager;
import com.frp.core.EventPublisher;
import com.frp.core.EventSubscriber;

import java.util.Map;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class ReactiveEventStream<T> implements EventPublisher<T> {

    // 6. Subscription management & Backpressure: Sử dụng ConcurrentHashMap để quản lý an toàn luồng
    private final Map<EventSubscriber<T>, SubscriptionManager<T>> subscriptions = new ConcurrentHashMap<>();

    // Hot Stream source: Events được tạo ra bất kể có Subscriber hay không
    private final BlockingQueue<T> eventQueue = new LinkedBlockingQueue<>();
    // Performance: Sử dụng FixedThreadPool để xử lý phát hành không chặn luồng chính
    private final ExecutorService processingPool = Executors.newFixedThreadPool(4);

    private volatile boolean isRunning = true;

    /** Constructor: Khởi động luồng xử lý nền (Hot Stream mechanism). */
    public ReactiveEventStream() {
        processingPool.submit(this::processQueue);
    }

    /** Luồng nền xử lý và phát hành sự kiện từ hàng đợi. */
    private void processQueue() {
        while (isRunning) {
            try {
                // Blocking call: Lấy sự kiện và tạm dừng nếu hàng đợi trống
                T event = eventQueue.take();

                // Phát hành sự kiện đến tất cả các Subscriber một cách song song
                subscriptions.values().parallelStream().forEach(manager -> {
                    // 2. Backpressure check: Chỉ phát hành nếu Subscriber đã yêu cầu
                    if (!manager.tryEmit(event)) {
                        // Stalling/Dropping: Hành vi mặc định khi áp lực ngược xảy ra
                        System.out.println("[Publisher] Dropping event due to backpressure: " + event);
                    }
                });
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                isRunning = false;
                break;
            }
        }
    }

    @Override
    public void subscribe(EventSubscriber<T> subscriber) {
        SubscriptionManager<T> manager = new SubscriptionManager<>(subscriber);
        subscriptions.put(subscriber, manager);
        System.out.println("New subscriber registered.");

        // Kích hoạt yêu cầu ban đầu (khởi động Backpressure)
        manager.request(5);
    }

    @Override
    public void unsubscribe(EventSubscriber<T> subscriber) {
        subscriptions.remove(subscriber);
        System.out.println("Subscriber unregistered.");
    }

    /** Cho phép Subscriber yêu cầu thêm dữ liệu (Backpressure request). */
    public void requestMore(EventSubscriber<T> subscriber, long n) {
        SubscriptionManager<T> manager = subscriptions.get(subscriber);
        if (manager != null) {
            manager.request(n);
        }
    }

    @Override
    public void publish(T event) {
        // Hot Stream: Đưa event vào hàng đợi
        eventQueue.offer(event);
    }

    /** Báo hiệu kết thúc và giải phóng tài nguyên. */
    public void complete() {
        isRunning = false;
        processingPool.shutdownNow();

        // Sửa lỗi cú pháp: Gọi onComplete() trên Subscriber thông qua Manager
        subscriptions.values().forEach(manager -> EventSubscriber.onComplete());
        System.out.println("Reactive Stream Completed.");
    }

    // --- 4. Stream Composition (Operators) ---

    /** Operator: map (Ánh xạ 1:1) */
    public <R> ReactiveEventStream<R> map(Function<T, R> mapper) {
        ReactiveEventStream<R> mappedStream = new ReactiveEventStream<>();

        this.subscribe(new EventSubscriber<T>() {
            @Override
            public void onNext(T event) {
                try {
                    R mappedEvent = mapper.apply(event);
                    mappedStream.publish(mappedEvent);
                } catch (Exception e) {
                    onError(e); // Chuyển lỗi xuống
                }
            }
            @Override
            public void onError(Throwable error) { mappedStream.subscriptions.values().forEach(m -> m.getSubscriber().onError(error)); }
            public void onComplete() { mappedStream.subscriptions.values().forEach(m -> EventSubscriber.onComplete()); }
        });

        return mappedStream;
    }

    /** Operator: filter (Lọc) */
    public ReactiveEventStream<T> filter(Predicate<T> predicate) {
        ReactiveEventStream<T> filteredStream = new ReactiveEventStream<>();

        this.subscribe(new EventSubscriber<T>() {
            @Override
            public void onNext(T event) {
                if (predicate.test(event)) {
                    filteredStream.publish(event);
                }
            }
            @Override
            public void onError(Throwable error) { filteredStream.subscriptions.values().forEach(m -> m.getSubscriber().onError(error)); }
            public void onComplete() { filteredStream.subscriptions.values().forEach(m -> EventSubscriber.onComplete()); }
        });

        return filteredStream;
    }

    /** Operator: flatMap (Ánh xạ 1:N) */
    public <R> ReactiveEventStream<R> flatMap(Function<T, Stream<R>> mapper) {
        ReactiveEventStream<R> flatMapped = new ReactiveEventStream<>();

        this.subscribe(new EventSubscriber<T>() {
            @Override
            public void onNext(T event) {
                try {
                    // Áp dụng Stream API để làm phẳng
                    mapper.apply(event).forEach(flatMapped::publish);
                } catch (Exception e) {
                    onError(e);
                }
            }
            @Override
            public void onError(Throwable error) { flatMapped.subscriptions.values().forEach(m -> m.getSubscriber().onError(error)); }
            public void onComplete() { flatMapped.subscriptions.values().forEach(m -> EventSubscriber.onComplete()); }
        });
        return flatMapped;
    }

    /** Operator: onErrorResume (3. Chiến lược phục hồi lỗi) */
    public ReactiveEventStream<T> onErrorResume(Function<Throwable, Stream<T>> recoveryStreamSupplier) {
        ReactiveEventStream<T> recoveredStream = new ReactiveEventStream<>();

        this.subscribe(new EventSubscriber<T>() {
            @Override
            public void onNext(T event) {
                recoveredStream.publish(event);
            }

            @Override
            public void onError(Throwable error) {
                System.err.println("Error encountered: " + error.getMessage() + ". Attempting recovery.");
                try {
                    // Phát hành stream phục hồi thay thế
                    recoveryStreamSupplier.apply(error).forEach(recoveredStream::publish);

                    // Sau khi phục hồi, hoàn thành stream
                    recoveredStream.subscriptions.values().forEach(m -> EventSubscriber.onComplete());
                } catch (Exception e) {
                    // Lỗi trong quá trình phục hồi, chuyển tiếp lỗi gốc
                    recoveredStream.subscriptions.values().forEach(m -> m.getSubscriber().onError(error));
                }
            }

            public void onComplete() {
                recoveredStream.subscriptions.values().forEach(m -> EventSubscriber.onComplete());
            }
        });
        return recoveredStream;
    }
}