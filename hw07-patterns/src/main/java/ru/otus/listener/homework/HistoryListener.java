package ru.otus.listener.homework;

import ru.otus.listener.Listener;
import ru.otus.model.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HistoryListener implements Listener, HistoryReader {

  private final Map<Long, Message> history;

  public HistoryListener() {
    history = new HashMap<>();
  }

  @Override
  public void onUpdated(Message msg) {
    history.put(msg.getId(), msg.toBuilder().build());
  }

  @Override
  public Optional<Message> findMessageById(long id) {
    return Optional.ofNullable(history.get(id));
  }
}
