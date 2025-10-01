package com.example.app;

import com.example.framework.anno.*;

@Service
@Singleton
public class UserService {
    public void sayHello() {
        System.out.println("Xin chào từ UserService!");
    }
}
