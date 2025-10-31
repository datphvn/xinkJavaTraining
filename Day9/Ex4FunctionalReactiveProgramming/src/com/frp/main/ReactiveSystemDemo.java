package com.frp.main;

import com.frp.core.EventSubscriber;
import com.frp.impl.ReactiveEventStream;

import java.util.Arrays;
import java.util.stream.Stream;

public class ReactiveSystemDemo {

    public static void main(String[] args) throws InterruptedException {

        // 1. Khởi tạo Publisher gốc (Source Stream)
        ReactiveEventStream<Integer> sourceStream = new ReactiveEventStream<>();

        // 2. Định nghĩa Subscriber Terminal (Sink)
        EventSubscriber<String> finalSubscriber = new EventSubscriber<String>() {
            @Override
            public void onNext(String event) {
                System.out.println("[Subscriber] Received: " + event);
                // Sau khi nhận 1 event, yêu cầu thêm 2 events
                ((ReactiveEventStream<Integer>) sourceStream).requestMore(this, 2);
            }
            @Override
            public void onError(Throwable error) {
                System.err.println("[Subscriber] Error occurred: " + error.getMessage());
            }
            @Override
            public void onComplete() {
                System.out.println("[Subscriber] Stream finished.");
            }
        };

        // 3. Xây dựng Pipeline (Composition)
        ReactiveEventStream<String> processedStream = sourceStream
                // Filter: Chỉ lấy số chẵn
                .filter(n -> n % 2 == 0)

                // Map: Nhân đôi giá trị
                .map(n -> n * 2)

                // onErrorResume: Phục hồi lỗi bằng cách phát hành giá trị cố định
                .onErrorResume(error -> {
                    return Stream.of(999); // Trả về 999 khi có lỗi
                })

                // FlatMap: Chuyển đổi mỗi số thành 2 chuỗi (Bình thường và Ghi nhật ký)
                .flatMap(n -> Arrays.asList(
                        String.format("Result: %d", n),
                        String.format("LOG: Processed %d", n)
                ).stream());

        // 4. Đăng ký Subscriber
        processedStream.subscribe(finalSubscriber);

        // 5. Phát hành sự kiện
        // Stream chạy nóng (Hot Stream): Sẽ phát ra ngay cả khi chưa có yêu cầu đủ
        sourceStream.publish(1); // Lẻ (bị filter)
        sourceStream.publish(2); // Chẵn -> được yêu cầu
        sourceStream.publish(3); // Lẻ (bị filter)
        sourceStream.publish(4); // Chẵn -> được yêu cầu
        sourceStream.publish(6); // Chẵn -> được yêu cầu

        // Mô phỏng lỗi (sẽ kích hoạt onErrorResume)
        // sourceStream.publish(null); // Sẽ gây lỗi NullPointerException trong map

        // Chờ xử lý
        Thread.sleep(500);

        sourceStream.publish(8); // Chẵn -> được yêu cầu
        sourceStream.publish(10); // Chẵn -> được yêu cầu

        Thread.sleep(500);

        sourceStream.publish(12); // Chẵn -> Hàng đợi sẽ bị Backpressure
        sourceStream.publish(14); // Chẵn -> Hàng đợi sẽ bị Backpressure

        // 6. Hoàn thành
        sourceStream.complete();
    }
}