package ru.otus.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestLogging implements TestLoggingInterface {
  private static final Logger logger = LoggerFactory.getLogger(TestLogging.class);

  @Log
  @Override
  public void calculation(int param) {
    logger.info(String.valueOf(param));
  }

  @Log
  @Override
  public void calculation(int param1, int param2) {
    logger.info(String.valueOf(param1 + param2));
  }

  @Log
  @Override
  public void calculation(int param1, int param2, String param3) {
    logger.info(String.valueOf(param1 + param2 + Integer.parseInt(param3)));
  }

  @Log
  @Override
  public void calculation(int param1, int param2, int param3) {
    logger.info(String.valueOf(param1 + param2 + param3));
  }

  @Log
  @Override
  public void calculation() {
    logger.info(String.valueOf(0));
  }

}
