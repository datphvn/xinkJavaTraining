package com.example.framework.anno;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE) // annotation dùng cho class
public @interface Service {
    String value() default "";
}
