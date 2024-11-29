package ru.otus.dto;

import java.lang.reflect.Method;
import java.util.Set;

public class TestMethodsDto {
    private Set<Method> testMethods;
    private Set<Method> beforeMethods;
    private Set<Method> afterMethods;

    public TestMethodsDto(Set<Method> testMethods, Set<Method> beforeMethods, Set<Method> afterMethods) {
        this.testMethods = testMethods;
        this.beforeMethods = beforeMethods;
        this.afterMethods = afterMethods;
    }

    public Set<Method> getTestMethods() {
        return testMethods;
    }

    public void setTestMethods(Set<Method> testMethods) {
        this.testMethods = testMethods;
    }

    public Set<Method> getBeforeMethods() {
        return beforeMethods;
    }

    public void setBeforeMethods(Set<Method> beforeMethods) {
        this.beforeMethods = beforeMethods;
    }

    public Set<Method> getAfterMethods() {
        return afterMethods;
    }

    public void setAfterMethods(Set<Method> afterMethods) {
        this.afterMethods = afterMethods;
    }
}
