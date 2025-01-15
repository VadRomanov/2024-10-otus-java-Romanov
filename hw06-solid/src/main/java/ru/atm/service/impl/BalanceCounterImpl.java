package ru.atm.service.impl;

import ru.atm.service.BalanceCounter;
import ru.atm.service.Storage;

public class BalanceCounterImpl implements BalanceCounter {

  private final Storage storage;

  public BalanceCounterImpl(Storage storage) {
    this.storage = storage;
  }

  @Override
  public int countBalance() {
    var cells = storage.getCellDenominations();
    if (cells.isEmpty()) {
      return 0;
    }

    int result = 0;
    for (var cell : cells) {
      var value = storage.getCellValue(cell);
      result += value * cell.getValue();
    }

    return result;
  }
}
