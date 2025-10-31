package com.frp.core;

// 1. Event stream creation và consumption
public interface EventPublisher<T> {
    // Subscriber đăng ký với Publisher
    void subscribe(EventSubscriber<T> subscriber);

    // (Optional) Cho phép Subscriber hủy đăng ký
    void unsubscribe(EventSubscriber<T> subscriber);

    // Phương thức nội bộ để đẩy event vào stream
    void publish(T event);
}