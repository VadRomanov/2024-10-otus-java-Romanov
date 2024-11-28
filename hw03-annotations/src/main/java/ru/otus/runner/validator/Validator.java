package ru.otus.runner.validator;

import ru.otus.annotations.After;
import ru.otus.annotations.Before;
import ru.otus.annotations.Test;

import java.lang.annotation.Annotation;
import java.util.List;

public class Validator {
    public void validateAnnotations(List<Annotation> annotations, String methodName) {
        List<?> annotationClasses = annotations.stream().map(Annotation::annotationType).toList();
        if (List.of(Test.class, Before.class).containsAll(annotationClasses)) {
            throw new UnsupportedOperationException("Method %s may not have both @Test and @Before annotations".formatted(methodName));
        }
        if (List.of(Test.class, After.class).containsAll(annotationClasses)) {
            throw new UnsupportedOperationException("Method %s may not have both @Test and @After annotations".formatted(methodName));
        }
        if (List.of(Before.class, After.class).containsAll(annotationClasses)) {
            throw new UnsupportedOperationException("Method %s may not have both @Before and @After annotations".formatted(methodName));
        }
    }
}
