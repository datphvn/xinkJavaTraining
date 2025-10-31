package com.frp.core;

// 1. Event stream creation và consumption
public interface EventSubscriber<T> {
    // Phương thức nhận event tiếp theo
    void onNext(T event);

    // Phương thức xử lý lỗi (3. Error recovery)
    void onError(Throwable error);

    // Phương thức báo hiệu kết thúc stream
    static void onComplete();
}