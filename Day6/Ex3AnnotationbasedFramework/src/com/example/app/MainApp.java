package com.example.app;

import com.example.framework.core.AnnotationProcessor;

public class MainApp {
    public static void main(String[] args) {
        AnnotationProcessor processor = new AnnotationProcessor();

        // Đăng ký service
        processor.registerService(UserService.class);

        // Tạo controller và inject service
        UserController controller = new UserController();
        processor.injectDependencies(controller);

        // Gọi method có @Loggable
        processor.invokeWithLogging(controller, "processUser");
    }
}
