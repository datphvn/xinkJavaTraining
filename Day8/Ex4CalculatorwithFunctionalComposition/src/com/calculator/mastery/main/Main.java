package com.calculator.mastery.main;

import com.calculator.mastery.core.FunctionalCalculatorCore.MathFunction;
import com.calculator.mastery.core.FunctionalCalculatorCore.MathFunctions;
import com.calculator.mastery.core.FunctionalCalculatorCore.CalculatorManager;
import com.calculator.mastery.core.FunctionalCalculatorCore.Evaluator;

public class Main {

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("  EXERCISE 4: FUNCTIONAL CALCULATOR COMPOSITION  ");
        System.out.println("=================================================");

        CalculatorManager manager = new CalculatorManager();
        Evaluator evaluator = new Evaluator(manager);

        double inputValue = 2.0;

        System.out.printf("\n[INPUT] Giá trị đầu vào (x): %.2f%n", inputValue);

        // --- 1. Function Composition (g(f(x))) ---
        // Hàm phức tạp: f(x) = (x^2 + 5) / 2
        // Từng bước: SQUARE -> plus(5) -> times(0.5)

        MathFunction f_x = MathFunctions.SQUARE      // x^2
                .plus(5.0)      // x^2 + 5
                .times(0.5);    // (x^2 + 5) / 2

        double result_fx = evaluator.calculateAndLog("f(x) = (x^2 + 5) / 2", f_x, inputValue);
        System.out.printf("[RESULT] f(%.2f) = %.4f%n", inputValue, result_fx); // (4+5)/2 = 4.5

        // --- 2. Composition Chaining (andThen / compose) ---
        // Hàm: g(f(x)) = sin(f(x))

        MathFunction g_fx = f_x.andThen(MathFunctions.SIN); // f_x rồi SIN

        double result_g_fx = evaluator.calculateAndLog("g(f(x)) = sin((x^2 + 5) / 2)", g_fx, inputValue);
        System.out.printf("[RESULT] g(%.2f) = sin(4.5) = %.4f%n", inputValue, result_g_fx);

        // --- 3. Custom Function Definition và Re-use ---
        // Định nghĩa hàm tùy chỉnh: poly(x) = 1 + 2x + 3x^2
        MathFunction polynomialFunc = MathFunctions.polynomial(1, 2, 3);
        manager.defineFunction("POLY", polynomialFunc);

        // Sử dụng hàm tùy chỉnh trong Composition
        MathFunction h_x = manager.getFunction("POLY") // 1 + 2x + 3x^2
                .compose(MathFunctions.LOG) // POLY(log(x))
                .inverse(); // 1 / POLY(log(x))

        double result_h_x = evaluator.calculateAndLog("h(x) = 1 / POLY(log(x))", h_x, 10.0); // Test với x=10
        System.out.printf("[RESULT] h(10.0) = %.4f%n", result_h_x);

        // --- 4. Variable Substitution ---
        // Tính toán giá trị của biến E
        double result_E = evaluator.substituteAndCalculate("E");
        System.out.printf("[RESULT] Giá trị của E: %.4f%n", result_E);

        // Đặt biến mới và tính toán
        manager.setVariable("A", 16.0);
        double result_A = evaluator.substituteAndCalculate("A");
        System.out.printf("[RESULT] Giá trị của A: %.4f%n", result_A);

        MathFunction sqrt_A = MathFunctions.SQRT.compose(MathFunctions.constant(manager.getVariable("A")));
        double result_sqrt_A = evaluator.calculateAndLog("sqrt(A)", sqrt_A, 0.0);
        System.out.printf("[RESULT] sqrt(A) = %.4f%n", result_sqrt_A); // sqrt(16) = 4

        // --- 5. History và Replay ---
        System.out.println("\n--- LỊCH SỬ TÍNH TOÁN ---");
        manager.getHistory().forEach(System.out::println);

        System.out.println("\n--- REPLAY ---");
        manager.replayLast();
    }
}
