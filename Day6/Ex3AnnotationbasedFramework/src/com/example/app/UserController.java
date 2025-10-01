package com.example.app;

import com.example.framework.anno.*;

public class UserController {
    @Inject
    private UserService userService;

    @Loggable
    public void processUser() {
        userService.sayHello();
        System.out.println("Đang xử lý người dùng...");
    }
}
