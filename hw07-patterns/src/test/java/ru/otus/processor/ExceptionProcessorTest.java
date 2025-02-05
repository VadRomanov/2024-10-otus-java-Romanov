package ru.otus.processor;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.otus.model.Message;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ExceptionProcessorTest {

  @ParameterizedTest
  @ValueSource(ints = {1, 2})
  void exceptionProcessorTest(int second) {
    // given
    var exceptionProcessor = new ExceptionProcessor(() -> LocalDateTime.of(2000, 1, 1, 0, 0, second));

    // when
    if (second % 2 == 0) {
      assertThrows(RuntimeException.class, () -> exceptionProcessor.process(new Message.Builder(1L).build()));
    } else {
      assertDoesNotThrow(() -> exceptionProcessor.process(new Message.Builder(1L).build()));
    }
  }
}