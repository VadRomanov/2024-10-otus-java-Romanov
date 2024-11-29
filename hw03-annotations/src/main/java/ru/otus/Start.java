package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.dto.TestsResultDto;
import ru.otus.runner.TestRunner;

public class Start {
    private static final Logger logger = LoggerFactory.getLogger(Start.class);

    public static void main(String[] args) {
        TestRunner testRunner = new TestRunner();
        for (String testClass : args) {
            try {
                TestsResultDto result = testRunner.runTests(testClass);
                logger.info("Tests: {}. Passed: {}. Failed: {}", result.getTotal(), result.getPassed(), result.getFailed());
            } catch (Exception e) {
                logger.error(e.toString());
            }
        }
    }
}
