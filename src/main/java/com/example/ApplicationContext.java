package com.example;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class ApplicationContext {

    private Map<Class<?>, Object> beans = new HashMap<>();



    public void scan(Class<?>... classes) {
        for (Class<?> clazz : classes) {
            if (clazz.isAnnotationPresent(Component.class)) {
                try {
                    Object instance = clazz.getDeclaredConstructor().newInstance();
                    beans.put(clazz, instance);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                
            }
        }
        injectDependencies();

    }

    private void injectDependencies() {
        for (Map.Entry<Class<?>, Object> entry : beans.entrySet()) {
            Field[] fields = entry.getValue().getClass().getDeclaredFields();
            for (Field f : fields) {
                if (f.isAnnotationPresent(Inject.class)) {
                    f.setAccessible(true);
                    Class<?> fieldType = f.getType();
                    if (!beans.containsKey(fieldType))
                        throw new RuntimeException("No bean found for type " + fieldType.getName());
                    try {
                        f.set(entry.getValue(), beans.get(fieldType));
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }

                }
            }
        }

    }

    public <T> T getBean(Class<T> type) {
        return (T) beans.get(type);
    }

}
