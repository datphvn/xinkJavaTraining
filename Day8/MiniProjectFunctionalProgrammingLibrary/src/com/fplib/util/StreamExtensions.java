package com.fplib.util;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class StreamExtensions {

    // --- 1. Zip operation ---
    // Kết hợp hai streams thành một stream mới bằng cách sử dụng BiFunction
    public static <A, B, C> Stream<C> zip(Stream<A> stream1,
                                          Stream<B> stream2,
                                          BiFunction<A, B, C> combiner) {
        Iterator<A> iter1 = stream1.iterator();
        Iterator<B> iter2 = stream2.iterator();

        // Sử dụng Stream.generate để lazy-generate các phần tử
        return (Stream<C>) Stream.generate(() -> {
                    if (iter1.hasNext() && iter2.hasNext()) {
                        return Optional.of(combiner.apply(iter1.next(), iter2.next()));
                    } else {
                        return Optional.empty();
                    }
                })
                .takeWhile(Optional::isPresent)
                .map(Optional::get);
    }

    // --- 2. Scan operation (Accumulating Fold) ---
    // Áp dụng một hàm tích lũy và trả về tất cả các giá trị trung gian, bao gồm cả identity
    public static <T, R> Stream<R> scan(Stream<T> stream, R identity, BiFunction<R, T, R> accumulator) {
        Iterator<T> iter = stream.iterator();

        // Sử dụng một mảng 1 phần tử để giữ trạng thái hiện tại (giá trị tích lũy)
        Object[] current = new Object[]{identity};

        // Tạo Stream vô hạn, sau đó dừng khi Iterator hết
        return Stream.concat(
                Stream.of(identity), // Bắt đầu bằng giá trị Identity
                (Stream<? extends R>) Stream.generate(() -> {
                            if (iter.hasNext()) {
                                // Tích lũy giá trị mới
                                current[0] = accumulator.apply((R)current[0], iter.next());
                                return Optional.of((R)current[0]);
                            } else {
                                return Optional.empty();
                            }
                        })
                        .takeWhile(Optional::isPresent)
                        .map(Optional::get)
        );
    }

    // --- 3. Window operation (Non-overlapping) ---
    // Chia stream thành các List (windows) có kích thước cố định
    public static <T> Stream<List<T>> window(Stream<T> stream, int size) {
        if (size <= 0) throw new IllegalArgumentException("Window size must be positive");
        Iterator<T> iter = stream.iterator();

        return Stream.generate(() -> {
                    List<T> window = new ArrayList<>(size);
                    for (int i = 0; i < size && iter.hasNext(); i++) {
                        window.add(iter.next());
                    }
                    // Trả về Optional chứa window nếu nó không rỗng
                    return window.isEmpty() ? Optional.<List<T>>empty() : Optional.of(window);
                })
                .takeWhile(Optional::isPresent)
                .map(Optional::get);
    }
}
