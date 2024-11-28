package ru.otus.runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.dto.ResultDto;
import ru.otus.reflection.ReflectionHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

public class MethodRunner {
    private static final Logger logger = LoggerFactory.getLogger(MethodRunner.class);

    public ResultDto invokeMethods(Method beforeMethod, Method afterMethod, Set<Method> testMethods, Class<?> clazz) {
        Integer passed = 0;
        Integer failed = 0;
        for (Method testMethod : testMethods) {
            Object object = ReflectionHelper.instantiate(clazz);
            try {
                invokeMethod(beforeMethod, object);
                boolean successful = invokeTestMethod(testMethod, object);
                invokeMethod(afterMethod, object);
                if (successful) {
                    passed++;
                } else {
                    failed++;
                }
            } catch (Exception e) {
                logger.error("Error occurred while running method: {}. Error: {}", e.getMessage(), e.getCause().getMessage());
                failed++;
            }
        }

        return new ResultDto(passed, failed);
    }

    @SuppressWarnings("java:S3011")
    private boolean invokeTestMethod(Method method, Object object) {
        method.setAccessible(true);
        try {
            method.invoke(object);
            return true;
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof AssertionError) {
                logger.error("Method {}{}", method.getName(), e.getCause().getMessage());
                return false;
            }
            throw new RuntimeException(method.getName(), e.getCause());
        } catch (IllegalAccessException e) {
            throw new RuntimeException(method.getName(), e);
        }
    }


    @SuppressWarnings("java:S3011")
    private void invokeMethod(Method method, Object object) {
        if (method != null) {
            method.setAccessible(true);
            try {
                method.invoke(object);
            } catch (InvocationTargetException e) {
                if (e.getCause() instanceof AssertionError) {
                    throw new AssertionError(method.getName(), e.getCause());
                }
                throw new RuntimeException(method.getName(), e.getCause());
            } catch (IllegalAccessException e) {
                throw new RuntimeException(method.getName(), e);
            }
        }
    }

}
