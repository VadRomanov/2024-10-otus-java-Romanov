package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.dto.ResultDto;
import ru.otus.runner.TestRunner;

public class Start {
    private static final Logger logger = LoggerFactory.getLogger(Start.class);

    public static void main(String[] args) {
        TestRunner testRunner = new TestRunner();
        for (String testClass : args) {
            try {
                ResultDto result = testRunner.startTesting(testClass);
                logger.info("Tests: {}. Passed: {}. Failed: {}", result.getTotal(), result.getPassed(), result.getFailed());
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
    }
}
