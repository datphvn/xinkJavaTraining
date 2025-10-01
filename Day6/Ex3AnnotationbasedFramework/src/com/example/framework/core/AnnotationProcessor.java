package com.example.framework.core;

import com.example.framework.anno.*;
import java.lang.reflect.*;
import java.util.*;

public class AnnotationProcessor {
    private final Map<Class<?>, Object> services = new HashMap<>();

    // Đăng ký service
    public void registerService(Class<?> clazz) {
        if (clazz.isAnnotationPresent(Service.class)) {
            try {
                Object instance = clazz.getDeclaredConstructor().newInstance();
                services.put(clazz, instance);
            } catch (Exception e) {
                throw new RuntimeException("Không thể tạo service: " + clazz, e);
            }
        }
    }

    // Tiêm phụ thuộc
    public void injectDependencies(Object target) {
        for (Field field : target.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Inject.class)) {
                Class<?> type = field.getType();
                Object dependency = services.get(type);
                if (dependency == null) {
                    throw new RuntimeException("Không tìm thấy service cho " + type);
                }
                field.setAccessible(true);
                try {
                    field.set(target, dependency);
                } catch (Exception e) {
                    throw new RuntimeException("Không inject được field: " + field, e);
                }
            }
        }
    }

    // Thực thi method có @Loggable
    public Object invokeWithLogging(Object target, String methodName, Object... args) {
        try {
            Method method = target.getClass().getMethod(methodName);
            if (method.isAnnotationPresent(Loggable.class)) {
                long start = System.currentTimeMillis();
                Object result = method.invoke(target, args);
                long end = System.currentTimeMillis();
                System.out.println("[LOG] " + methodName + " chạy trong " + (end - start) + "ms");
                return result;
            } else {
                return method.invoke(target, args);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
