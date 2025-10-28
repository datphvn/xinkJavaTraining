package com.fplib.main;

import com.fplib.collection.ImmutableList;
import com.fplib.monad.Try;
import com.fplib.util.Lazy;
import com.fplib.util.StreamExtensions;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Main {

    // Giả lập hàm đắt đỏ
    private static String expensiveOperation() {
        try {
            System.out.println("  [LOG] Expensive I/O operation starting...");
            Thread.sleep(500);
            System.out.println("  [LOG] Expensive I/O operation finished.");
            return "RESULT: " + LocalDateTime.now();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return "Error";
        }
    }

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("     FUNCTIONAL PROGRAMMING UTILITY LIBRARY DEMO   ");
        System.out.println("=================================================");

        // --- 1. Immutable List Demonstration ---
        System.out.println("\n--- 1. IMMUTABLE LIST (COLLECTION) ---");
        ImmutableList<Integer> numbers = ImmutableList.of(1, 2, 3, 4, 5);
        System.out.println("List gốc: " + numbers);

        // Map: (x -> x * 2)
        ImmutableList<Integer> doubled = numbers.map(x -> x * 2);
        System.out.println("Map (x*2): " + doubled);

        // Filter: (x > 6)
        ImmutableList<Integer> filtered = doubled.filter(x -> x > 6);
        System.out.println("Filter (x>6): " + filtered); // [8, 10]

        // Fold (Sum)
        int sum = filtered.fold(0, Integer::sum);
        System.out.println("Fold (Sum): " + sum); // 18

        // Drop & Take
        ImmutableList<Integer> partial = numbers.drop(1).take(3); // Drop 1 (-> [2,3,4,5]), Take 3 (-> [2,3,4])
        System.out.println("Drop 1 & Take 3: " + partial);

        // --- 2. Lazy Evaluation Demonstration ---
        System.out.println("\n--- 2. LAZY EVALUATION (Thread-Safe) ---");
        Lazy<String> lazyValue = Lazy.of(Main::expensiveOperation);

        System.out.println("A. Tạo Lazy Value (chưa tính toán): " + lazyValue.isComputed());

        // Lần gọi đầu tiên (sẽ tính toán)
        System.out.println("B. Lần gọi 1:");
        String val1 = lazyValue.get();
        System.out.println("   Value: " + val1);

        // Lần gọi thứ hai (sẽ lấy từ cache)
        System.out.println("C. Lần gọi 2:");
        String val2 = lazyValue.get();
        System.out.println("   Value: " + val2);

        // Lazy Map
        Lazy<Integer> lengthLazy = lazyValue.map(String::length);
        System.out.println("D. Lazy Map (chưa tính toán): " + lengthLazy.isComputed());
        System.out.println("E. Kết quả Lazy Map: " + lengthLazy.get());


        // --- 3. Monadic Try Demonstration (Functional Error Handling) ---
        System.out.println("\n--- 3. MONADIC TRY (Error Handling) ---");

        // Success Case: Parse thành công
        Try<Integer> safeParse1 = Try.of(() -> Integer.parseInt("123"));
        System.out.println("Try Parse 1: " + safeParse1);

        // Failure Case: Parse lỗi
        Try<Integer> safeParse2 = Try.of(() -> Integer.parseInt("abc"));
        System.out.println("Try Parse 2: " + safeParse2);

        // Map và FlatMap trên Success
        Try<Double> mappedSuccess = safeParse1
                .map(i -> i * 2) // 246
                .flatMap(i -> Try.success(i / 10.0)); // 24.6
        System.out.println("Map/FlatMap Success: " + mappedSuccess); // Success(24.6)

        // Map và FlatMap trên Failure (Không chạy mapper)
        Try<Double> mappedFailure = safeParse2
                .map(i -> i * 2) // Bỏ qua
                .flatMap(i -> Try.success(i / 10.0)); // Bỏ qua
        System.out.println("Map/FlatMap Failure: " + mappedFailure); // Failure(NumberFormatException)

        // Recover
        int recoveredValue = safeParse2.recover(e -> {
            System.out.println("  [RECOVER] Phục hồi từ lỗi: " + e.getMessage());
            return -1;
        }).get();
        System.out.println("Recovered Value: " + recoveredValue); // -1


        // --- 4. Stream Extensions Demonstration ---
        System.out.println("\n--- 4. STREAM EXTENSIONS ---");

        // Zip
        Stream<String> words = Stream.of("A", "B", "C", "D");
        Stream<Integer> indices = Stream.of(1, 2, 3, 4, 5); // Dài hơn

        List<String> zipped = StreamExtensions.zip(words, indices, (w, i) -> i + "-" + w)
                .toList();
        System.out.println("Zip (A, 1): " + zipped); // [1-A, 2-B, 3-C, 4-D]

        // Scan (Tích lũy tổng)
        Stream<Integer> data = Stream.of(1, 2, 3, 4);
        List<Integer> scanned = StreamExtensions.scan(data, 0, Integer::sum)
                .toList();
        System.out.println("Scan (Tổng tích lũy): " + scanned); // [0, 1, 3, 6, 10]

        // Window
        Stream<String> letters = Stream.of("A", "B", "C", "D", "E", "F", "G");
        List<List<String>> windows = StreamExtensions.window(letters, 3)
                .toList();
        System.out.println("Window (size 3): " + windows); // [[A, B, C], [D, E, F], [G]]
    }
}
