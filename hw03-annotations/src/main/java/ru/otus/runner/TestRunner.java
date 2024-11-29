package ru.otus.runner;

import ru.otus.annotations.After;
import ru.otus.annotations.Before;
import ru.otus.annotations.Test;
import ru.otus.dto.TestsResultDto;
import ru.otus.dto.TestMethodsDto;
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

    public TestsResultDto runTests(String testClassName) throws ClassNotFoundException {
        Class<?> testClass = Class.forName(testClassName);

        final TestMethodsDto testMethods = prepareTestMethods(testClass);
        return invokeTestMethods(testMethods, testClass);
    }

    private TestMethodsDto prepareTestMethods(Class<?> testClass) {
        Set<Method> testMethods = new HashSet<>();
        Set<Method> beforeMethod = new HashSet<>();
        Set<Method> afterMethod = new HashSet<>();

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
                    beforeMethod.add(method);
                }
                if (annotation instanceof After) {
                    afterMethod.add(method);
                }
            }
        }

        return new TestMethodsDto(testMethods, beforeMethod, afterMethod);
    }

    private TestsResultDto invokeTestMethods(TestMethodsDto testMethods, Class<?> testClass) {
        return methodRunner.runTests(testMethods, testClass);
    }

}
