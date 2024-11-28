package ru.otus.test;

import com.google.common.collect.Lists;
import ru.otus.annotations.After;
import ru.otus.annotations.Before;
import ru.otus.annotations.Test;
import ru.otus.service.TestService;

import java.util.ArrayList;
import java.util.List;

import static ru.otus.asserts.Assert.assertEquals;
import static ru.otus.asserts.Assert.assertTrue;

public class TestClass {
    private TestService<Integer> sut;
    private List<Integer> testValues;

    @Before
    void setup() {
        sut = new TestService<>();

        testValues = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            testValues.add(i);
        }
    }

    @After
    void afterEach() {
        sut = null;
        testValues = null;
    }

    @Test
    void testOrder() {
        List<Integer> actualResult = sut.reverser(testValues);

        assertEquals(Lists.reverse(testValues), actualResult);
    }

    @Test
    void testSize() {
        List<Integer> actualResult = sut.reverser(testValues);

        assertTrue(actualResult.size() == testValues.size());
    }
}
