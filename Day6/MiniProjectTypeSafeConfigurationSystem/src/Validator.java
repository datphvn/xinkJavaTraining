import annotations.*;

import java.lang.reflect.Field;

public class Validator {
    public static void validate(Object obj) {
        for (Field field : obj.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object value = field.get(obj);

                if (field.isAnnotationPresent(ConfigNotEmpty.class)) {
                    if (value == null || value.toString().isEmpty()) {
                        throw new RuntimeException("Field " + field.getName() + " cannot be empty!");
                    }
                }

                if (field.isAnnotationPresent(ConfigRange.class) && value instanceof Number) {
                    ConfigRange range = field.getAnnotation(ConfigRange.class);
                    int intVal = ((Number) value).intValue();
                    if (intVal < range.min() || intVal > range.max()) {
                        throw new RuntimeException("Field " + field.getName() + " out of range!");
                    }
                }

            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
