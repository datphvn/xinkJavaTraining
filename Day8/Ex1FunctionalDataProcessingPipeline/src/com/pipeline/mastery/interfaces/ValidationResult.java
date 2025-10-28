package com.pipeline.mastery.interfaces;

import com.pipeline.mastery.model.Record;
import java.util.Optional;
import java.util.function.Function;

// 1. Validation Result (Immutable)
public record ValidationResult(boolean isValid, String message) {
    public static ValidationResult valid() {
        return new ValidationResult(true, "OK");
    }

    public static ValidationResult invalid(String message) {
        return new ValidationResult(false, message);
    }
}

// 2. Throwing Function - Hỗ trợ xử lý checked exception trong Lambda
@FunctionalInterface
public interface ThrowingFunction<T, R, E extends Exception> {
    R apply(T t) throws E;

    // Static utility to wrap an exception-throwing function into an Optional-returning function
    static <T, R> Function<T, Optional<R>> lift(ThrowingFunction<T, R, Exception> function) {
        return t -> {
            try {
                return Optional.of(function.apply(t));
            } catch (Exception e) {
                return Optional.empty();
            }
        };
    }
}


// 3. Composable Data Validator
@FunctionalInterface
public interface DataValidator<T> {
    ValidationResult validate(T data);

    // Composition: VÀ (AND)
    default DataValidator<T> and(DataValidator<T> other) {
        return data -> {
            ValidationResult result1 = this.validate(data);
            if (!result1.isValid()) {
                return result1; // Short-circuiting: trả về lỗi đầu tiên
            }
            return other.validate(data);
        };
    }

    // Composition: HOẶC (OR)
    default DataValidator<T> or(DataValidator<T> other) {
        return data -> {
            ValidationResult result1 = this.validate(data);
            if (result1.isValid()) {
                return result1;
            }
            // Nếu lỗi, thử validate tiếp theo
            return other.validate(data);
        };
    }
}

// 4. Composable Data Transformer
@FunctionalInterface
public interface DataTransformer<T, R> extends Function<T, R> {

    // Composition: andThen (f.andThen(g) = g(f(x)))
    default <V> DataTransformer<T, V> andThen(DataTransformer<R, V> next) {
        return input -> next.apply(this.apply(input));
    }

    // HOF: Thêm chức năng logging
    default DataTransformer<T, R> withLogging(String stepName) {
        return input -> {
            long start = System.nanoTime();
            R result = this.apply(input);
            long duration = System.nanoTime() - start;

            System.out.printf("  [LOG] Step '%s' processed record ID %d in %.2f ms%n",
                    stepName,
                    input instanceof Record r ? r.id() : -1,
                    duration / 1_000_000.0);
            return result;
        };
    }
}
