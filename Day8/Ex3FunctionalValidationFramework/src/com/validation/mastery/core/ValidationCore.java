import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Function;

// --- 1. Validation Result (Immutable) ---
// Chứa thông tin về kết quả và danh sách lỗi (Localization/Grouping)
public static class ValidationResult {
    private final boolean valid;
    private final List<String> errors;

    private ValidationResult(boolean valid, List<String> errors) {
        this.valid = valid;
        this.errors = Collections.unmodifiableList(errors);
    }

    public boolean isValid() {
        return valid;
    }

    public List<String> getErrors() {
        return errors;
    }

    // Factory method cho kết quả hợp lệ
    public static ValidationResult valid() {
        return new ValidationResult(true, Collections.emptyList());
    }

    // Factory method cho lỗi đơn
    public static ValidationResult error(String errorMessage) {
        return new ValidationResult(false, List.of(errorMessage));
    }

    // Factory method để kết hợp nhiều lỗi
    public static ValidationResult combine(List<ValidationResult> results) {
        List<String> allErrors = new ArrayList<>();
        boolean overallValid = true;

        for (ValidationResult result : results) {
            if (!result.valid) {
                overallValid = false;
                allErrors.addAll(result.errors);
            }
        }
        return new ValidationResult(overallValid, allErrors);
    }

    @Override
    public String toString() {
        if (valid) return "ValidationResult: VALID";
        return "ValidationResult: INVALID (" + String.join("; ", errors) + ")";
    }
}


        // --- 2. Core Rule Interface (Composable Functional Interface) ---
        @FunctionalInterface
        public interface Rule<T> extends Function<T, ValidationResult> {

            // Composition: Rule AND Rule (Short-circuiting)
            default Rule<T> and(Rule<T> other) {
                return value -> {
                    ValidationResult result = this.apply(value);
                    // Nếu hợp lệ, chuyển sang Rule tiếp theo
                    if (result.isValid()) {
                        return other.apply(value);
                    }
                    // Nếu không, trả về lỗi ngay lập tức (short-circuiting)
                    return result;
                };
            }

            // Composition: Rule OR Rule (Fallback)
            default Rule<T> or(Rule<T> other) {
                return value -> {
                    ValidationResult result = this.apply(value);
                    // Nếu hợp lệ, trả về hợp lệ (không cần kiểm tra Rule kia)
                    if (result.isValid()) {
                        return result;
                    }
                    // Nếu không hợp lệ, thử Rule kia (fallback)
                    return other.apply(value);
                };
            }
        }


        // --- 3. Rule Builders (Factory Methods) ---
        public static class Rules {

            public static <T> Rule<T> notNull(String fieldName) {
                return value -> value != null
                        ? ValidationResult.valid()
                        : ValidationResult.error(fieldName + " cannot be null");
            }

            public static Rule<String> minLength(int minLength, String fieldName) {
                return value -> {
                    if (value == null) return ValidationResult.error(fieldName + " cannot be null for minLength check");
                    return value.length() >= minLength
                            ? ValidationResult.valid()
                            : ValidationResult.error(fieldName + " minimum length is " + minLength + ", found " + value.length());
                };
            }

            public static Rule<String> matches(String regex, String fieldName, String message) {
                return value -> {
                    if (value == null) return ValidationResult.error(fieldName + " cannot be null for regex check");
                    return value.matches(regex)
                            ? ValidationResult.valid()
                            : ValidationResult.error(fieldName + " is invalid. " + message);
                };
            }

            public static <T extends Comparable<T>> Rule<T> range(T min, T max, String fieldName) {
                return value -> {
                    if (value == null) return ValidationResult.error(fieldName + " cannot be null");
                    if (value.compareTo(min) < 0) return ValidationResult.error(fieldName + " value below minimum: " + min);
                    if (value.compareTo(max) > 0) return ValidationResult.error(fieldName + " value above maximum: " + max);
                    return ValidationResult.valid();
                };
            }

            // Custom Rule Builder
            public static <T> Rule<T> custom(Predicate<T> predicate, String errorMessage) {
                return value -> predicate.test(value)
                        ? ValidationResult.valid()
                        : ValidationResult.error(errorMessage);
            }
        }


        // --- 4. Cross-Field Validation Utility ---
// Rule nhận toàn bộ đối tượng T, áp dụng validation giữa các trường
        @FunctionalInterface
        public interface CrossFieldRule<T> extends Function<T, ValidationResult> {}


        // --- 5. Conditional Validation Utility ---
// HOF: Áp dụng Rule (R) chỉ khi điều kiện (C) đúng
        public static <T> Rule<T> conditional(Predicate<T> condition, Rule<T> rule) {
            return value -> {
                if (condition.test(value)) {
                    return rule.apply(value);
                }
                return ValidationResult.valid(); // Bỏ qua nếu điều kiện sai
            };
        }

        void main() {
        }
