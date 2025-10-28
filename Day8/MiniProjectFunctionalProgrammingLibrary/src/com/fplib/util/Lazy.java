package com.fplib.util;

import java.util.function.Function;
import java.util.function.Supplier;

// Lớp Lazy: Đảm bảo tính toán chỉ xảy ra MỘT LẦN và chỉ khi cần (thread-safe)
public class Lazy<T> implements Supplier<T> {
    private volatile boolean computed = false;
    private T value;
    private final Supplier<T> supplier;

    private Lazy(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    public static <T> Lazy<T> of(Supplier<T> supplier) {
        return new Lazy<>(supplier);
    }

    // Phương thức chính: Lấy giá trị, tính toán nếu chưa được tính
    @Override
    public T get() {
        if (!computed) {
            // Khóa đồng bộ hóa (synchronization) đảm bảo thread safety
            synchronized (this) {
                if (!computed) {
                    System.out.println("  [LAZY] Calculating value...");
                    value = supplier.get();
                    computed = true;
                }
            }
        }
        return value;
    }

    // Functional mapping
    public <R> Lazy<R> map(Function<T, R> mapper) {
        // Tạo một Lazy mới, hàm get của nó sẽ gọi get() của đối tượng hiện tại
        return Lazy.of(() -> mapper.apply(this.get()));
    }

    // Functional flatMap
    public <R> Lazy<R> flatMap(Function<T, Lazy<R>> mapper) {
        return Lazy.of(() -> mapper.apply(this.get()).get());
    }

    public boolean isComputed() {
        return computed;
    }
}
