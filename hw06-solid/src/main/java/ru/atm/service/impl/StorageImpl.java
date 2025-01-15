package ru.atm.service.impl;

import ru.atm.Denomination;
import ru.atm.service.Storage;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class StorageImpl implements Storage {

  private static final Map<Denomination, Integer> CELLS = new TreeMap<>();

  public void addToCell(Denomination cellDenomination, Integer amount) {
    int currentValue = CELLS.getOrDefault(cellDenomination, 0);
    int newValue = currentValue + amount;
    CELLS.put(cellDenomination, newValue);
  }

  public void extractFromCell(Denomination cellDenomination, Integer amount) {
    int currentValue = CELLS.getOrDefault(cellDenomination, 0);
    if (currentValue < amount) {
      throw new RuntimeException("The requested amount is greater than available");
    }

    int newValue = currentValue - amount;
    CELLS.put(cellDenomination, newValue);
  }

  public int getCellValue(Denomination cellDenomination) {
    return CELLS.getOrDefault(cellDenomination, 0);
  }

  public Set<Denomination> getCellDenominations() {
    return CELLS.keySet();
  }
}
