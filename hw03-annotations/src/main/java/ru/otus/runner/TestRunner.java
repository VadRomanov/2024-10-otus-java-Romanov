package ru.otus.runner;

import ru.otus.annotations.After;
import ru.otus.annotations.Before;
import ru.otus.annotations.Test;
import ru.otus.dto.ResultDto;
import ru.otus.runner.validator.Validator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TestRunner {
    private final Validator validator;
    private final MethodRunner methodRunner;

    public TestRunner() {
        validator = new Validator();
        methodRunner = new MethodRunner();
    }

    public ResultDto startTesting(String testClassName) throws ClassNotFoundException {
        Class<?> testClass = getTestClass(testClassName);

        Set<Method> testMethods = new HashSet<>();
        Method beforeMethod = null;
        Method afterMethod = null;

        for (Method method : testClass.getDeclaredMethods()) {
            List<Annotation> annotations = List.of(method.getAnnotations());
            if (annotations.size() > 1) {
                validator.validateAnnotations(annotations, method.getName());
            }
            for (Annotation annotation : annotations) {
                if (annotation instanceof Test) {
                    testMethods.add(method);
                }
                if (annotation instanceof Before) {
                    beforeMethod = method;
                }
                if (annotation instanceof After) {
                    afterMethod = method;
                }
            }
        }

        return methodRunner.invokeMethods(beforeMethod, afterMethod, testMethods, testClass);
    }

    private Class<?> getTestClass(String testClassName) throws ClassNotFoundException {
        String classFullName = "ru.otus.test.%s".formatted(testClassName);
        Class<?> testClass;
        try {
            testClass = Class.forName(classFullName);
        } catch (ClassNotFoundException e) {
            throw new ClassNotFoundException("Class %s not found.".formatted(classFullName));
        }
        return testClass;
    }

}
