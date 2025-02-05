package ru.otus.processor;

import ru.otus.model.Message;

public class ExceptionProcessor implements Processor {
  private final DateTimeProvider dateTimeProvider;

  public ExceptionProcessor(DateTimeProvider dateTimeProvider) {
    this.dateTimeProvider = dateTimeProvider;
  }

  @Override
  public Message process(Message message) {
    if (dateTimeProvider.getDate().getSecond() % 2 == 0) {
      throw new RuntimeException("Even second");
    } else {
      return null;
    }
  }
}
