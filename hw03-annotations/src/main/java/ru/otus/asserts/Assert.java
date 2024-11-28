package ru.otus.asserts;

import java.util.List;

public class Assert {

    private Assert() {
    }

    public static void assertEquals(List<?> expected, List<?> actual) {
        if (!expected.equals(actual)) {
            throw new AssertionError("%nExpected: %s%nActual: %s".formatted(expected, actual));
        }
    }

    public static void assertTrue(boolean value) {
        if (!value) {
            throw new AssertionError("\nExpected: True\nActual: False");
        }
    }

}
