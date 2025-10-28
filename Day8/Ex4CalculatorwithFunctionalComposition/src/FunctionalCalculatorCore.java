package com.calculator.mastery.core;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.function.BiFunction;

public class FunctionalCalculatorCore {

    // --- 1. Mathematical Function Interface (Composable) ---
    @FunctionalInterface
    public interface MathFunction {
        // SAM: Áp dụng hàm cho giá trị x
        double apply(double x);

        // Composition: g(f(x)) - Áp dụng 'before' trước, sau đó áp dụng 'this'
        default MathFunction compose(MathFunction before) {
            return x -> this.apply(before.apply(x));
        }

        // Composition: f(g(x)) - Áp dụng 'this' trước, sau đó áp dụng 'after'
        default MathFunction andThen(MathFunction after) {
            return x -> after.apply(this.apply(x));
        }

        // Operation: f(x) + constant
        default MathFunction plus(double constant) {
            return x -> this.apply(x) + constant;
        }

        // Operation: f(x) * factor
        default MathFunction times(double factor) {
            return x -> this.apply(x) * factor;
        }

        // Operation: 1 / f(x)
        default MathFunction inverse() {
            return x -> 1.0 / this.apply(x);
        }
    }

    // --- 2. Built-in Mathematical Functions và Factories ---
    public static class MathFunctions {
        public static final MathFunction IDENTITY = x -> x;
        public static final MathFunction SQUARE = x -> x * x;
        public static final MathFunction CUBE = x -> x * x * x;
        public static final MathFunction SQRT = Math::sqrt;
        public static final MathFunction SIN = Math::sin;
        public static final MathFunction COS = Math::cos;
        public static final MathFunction LOG = Math::log;
        public static final MathFunction EXP = Math::exp;

        // Function factory: Tạo hàm đa thức (a + bx + cx^2 + ...)
        public static MathFunction polynomial(double... coefficients) {
            return x -> {
                double result = 0;
                double power = 1;
                for (double coeff : coefficients) {
                    result += coeff * power;
                    power *= x;
                }
                return result;
            };
        }

        // Function factory: Tạo hàm lũy thừa x^exponent
        public static MathFunction power(double exponent) {
            return x -> Math.pow(x, exponent);
        }

        // Function factory: Hàm hằng số
        public static MathFunction constant(double value) {
            return x -> value;
        }
    }

    // --- 3. Calculator Manager (Lưu trữ lịch sử, biến, và hàm tùy chỉnh) ---
    public static class CalculatorManager {
        // Biến (Variable Substitution)
        private final Map<String, Double> variables = new LinkedHashMap<>();
        // Hàm tùy chỉnh (Custom Function Definitions)
        private final Map<String, MathFunction> customFunctions = new LinkedHashMap<>();
        // Lịch sử (Calculator History)
        private final List<String> history = new ArrayList<>();

        public CalculatorManager() {
            // Khởi tạo biến mặc định
            variables.put("PI", Math.PI);
            variables.put("E", Math.E);
            // Khởi tạo hàm mặc định
            customFunctions.put("ID", MathFunctions.IDENTITY);
            customFunctions.put("SQR", MathFunctions.SQUARE);
        }

        public void setVariable(String name, double value) {
            variables.put(name, value);
            System.out.printf("[MANAGER] Biến '%s' được đặt thành %.4f%n", name, value);
        }

        public double getVariable(String name) {
            return variables.getOrDefault(name, Double.NaN);
        }

        public void defineFunction(String name, MathFunction function) {
            customFunctions.put(name, function);
            System.out.printf("[MANAGER] Hàm tùy chỉnh '%s' đã được định nghĩa.%n", name);
        }

        public MathFunction getFunction(String name) {
            return customFunctions.get(name);
        }

        public void logHistory(String expression, double result) {
            String entry = String.format("[%s] f(x) = %s => %.4f",
                    java.time.LocalTime.now(),
                    expression,
                    result);
            history.add(entry);
        }

        public List<String> getHistory() {
            return Collections.unmodifiableList(history);
        }

        // Replay: Chạy lại lần tính toán cuối cùng trong lịch sử (minh họa)
        public void replayLast() {
            if (history.isEmpty()) {
                System.out.println("[MANAGER] Lịch sử trống, không thể replay.");
                return;
            }
            String lastEntry = history.get(history.size() - 1);
            System.out.println("[MANAGER] Replaying: " + lastEntry);
        }
    }

    // --- 4. Expression Evaluator (Simplified) ---
    // Vì Expression Parsing đầy đủ rất phức tạp, chúng ta sẽ mô phỏng nó bằng cách
    // xây dựng hàm bằng code và log/gọi nó thông qua Manager.

    public static class Evaluator {
        private final CalculatorManager manager;

        public Evaluator(CalculatorManager manager) {
            this.manager = manager;
        }

        // Thực hiện tính toán và ghi log
        public double calculateAndLog(String expression, MathFunction func, double inputValue) {
            double result = func.apply(inputValue);
            manager.logHistory(expression, result);
            return result;
        }

        // Variable Substitution (Áp dụng cho hàm hằng)
        public double substituteAndCalculate(String variableName) {
            double value = manager.getVariable(variableName);
            if (Double.isNaN(value)) {
                System.err.println("[EVAL] Biến không xác định: " + variableName);
                return Double.NaN;
            }
            // Áp dụng Identity function để tính toán (vì nó là hằng số)
            MathFunction constantFunc = MathFunctions.constant(value);
            return calculateAndLog("const(" + variableName + ")", constantFunc, 0);
        }
    }
}
