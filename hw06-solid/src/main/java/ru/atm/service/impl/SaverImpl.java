package ru.atm.service.impl;

import ru.atm.Denomination;
import ru.atm.service.Saver;
import ru.atm.service.Storage;

public class SaverImpl implements Saver {
  private final Storage storage;

  public SaverImpl(Storage storage) {
    this.storage = storage;
  }

  @Override
  public void save(Denomination denomination, Integer amount) {
    storage.addToCell(denomination, amount);
  }
}
