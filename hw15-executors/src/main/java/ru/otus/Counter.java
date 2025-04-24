package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Counter {
  private static final Logger logger = LoggerFactory.getLogger(Counter.class);
  private static final int MIN_VALUE = 1;
  private static final int MAX_VALUE = 10;

  private String lastThreadName = "Поток 2";
  private int lastValue = 0;
  private int nextValue = 1;

  private synchronized void action(boolean swip) {
    while (!Thread.currentThread().isInterrupted()) {
      try {
        while (lastThreadName.equals(Thread.currentThread().getName())) {
          this.wait();
        }

        logger.info("{}: {}", Thread.currentThread().getName(), nextValue);
        if (lastValue < nextValue && nextValue <= MAX_VALUE) {
          if (swip) {
            lastValue = nextValue;
            nextValue = nextValue == MAX_VALUE ? nextValue - 1 : nextValue + 1;
          }
        } else if (lastValue > nextValue && nextValue >= MIN_VALUE) {
          if (swip) {
            lastValue = nextValue;
            nextValue = nextValue == MIN_VALUE ? nextValue + 1 : nextValue - 1;
          }
        }
        lastThreadName = Thread.currentThread().getName();
        sleep();
        notifyAll();
      } catch (InterruptedException ex) {
        Thread.currentThread().interrupt();
      }
    }
  }

  public static void main(String[] args) {

    Counter counter = new Counter();

    new Thread(() -> counter.action(false), "Поток 1").start();
    new Thread(() -> counter.action(true), "Поток 2").start();
  }

  private static void sleep() {
    try {
      Thread.sleep(1_000);
    } catch (InterruptedException e) {
      logger.error(e.getMessage());
      Thread.currentThread().interrupt();
    }
  }

}
