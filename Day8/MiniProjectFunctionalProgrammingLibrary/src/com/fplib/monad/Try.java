package com.fplib.monad;

import java.util.function.Function;
import java.util.function.Supplier;

// --- 1. Abstract Try Type ---
public abstract class Try<T> {

    public abstract boolean isSuccess();
    public abstract boolean isFailure();
    public abstract T get();
    public abstract Exception getException();

    // Ánh xạ (Map): Áp dụng mapper nếu là Success, giữ nguyên Failure
    public abstract <R> Try<R> map(Function<T, R> mapper);

    // Ánh xạ phẳng (FlatMap): Áp dụng mapper nếu là Success, trả về Try mới
    public abstract <R> Try<R> flatMap(Function<T, Try<R>> mapper);

    // Phục hồi (Recover): Cung cấp giá trị mặc định nếu là Failure
    public abstract Try<T> recover(Function<Exception, T> recovery);

    // Factory method: Tạo Try từ một Supplier có thể ném exception
    public static <T> Try<T> of(ThrowingSupplier<T> supplier) {
        try {
            return success(supplier.get());
        } catch (Exception e) {
            return failure(e);
        }
    }

    public static <T> Try<T> success(T value) {
        return new Success<>(value);
    }

    public static <T> Try<T> failure(Exception exception) {
        return new Failure<>(exception);
    }

    // Utility interface cho Supplier có ném exception
    @FunctionalInterface
    public interface ThrowingSupplier<T> {
        T get() throws Exception;
    }
}

// --- 2. Concrete Success Class ---
class Success<T> extends Try<T> {
    private final T value;

    Success(T value) { this.value = value; }

    @Override public boolean isSuccess() { return true; }
    @Override public boolean isFailure() { return false; }
    @Override public T get() { return value; }
    @Override public Exception getException() { throw new UnsupportedOperationException("Success has no exception"); }

    @Override
    public <R> Try<R> map(Function<T, R> mapper) {
        return Try.of(() -> mapper.apply(value));
    }

    @Override
    public <R> Try<R> flatMap(Function<T, Try<R>> mapper) {
        // Áp dụng mapper, trả về Try mới (đã bọc lỗi nếu có)
        try {
            return mapper.apply(value);
        } catch (Exception e) {
            return Try.failure(e);
        }
    }

    @Override
    public Try<T> recover(Function<Exception, T> recovery) {
        return this; // Success không cần phục hồi
    }

    @Override
    public String toString() { return "Success(" + value + ")"; }
}

// --- 3. Concrete Failure Class ---
class Failure<T> extends Try<T> {
    private final Exception exception;

    Failure(Exception exception) { this.exception = exception; }

    @Override public boolean isSuccess() { return false; }
    @Override public boolean isFailure() { return true; }
    @Override public T get() { throw new RuntimeException("Cannot get value from Failure", exception); }
    @Override public Exception getException() { return exception; }

    @Override
    @SuppressWarnings("unchecked")
    public <R> Try<R> map(Function<T, R> mapper) {
        return (Try<R>) this; // Giữ nguyên lỗi
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R> Try<R> flatMap(Function<T, Try<R>> mapper) {
        return (Try<R>) this; // Giữ nguyên lỗi
    }

    @Override
    public Try<T> recover(Function<Exception, T> recovery) {
        // Phục hồi: Trả về một Success mới với giá trị phục hồi
        return Try.success(recovery.apply(exception));
    }

    @Override
    public String toString() { return "Failure(" + exception.getClass().getSimpleName() + ": " + exception.getMessage() + ")"; }
}
