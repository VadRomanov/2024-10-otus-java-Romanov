package ru.atm.service.impl;

import ru.atm.Denomination;
import ru.atm.service.Storage;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class StorageImpl implements Storage {

  private final Map<Denomination, Integer> cells;

  public StorageImpl() {
    this.cells = new TreeMap<>();
  }

  public void addToCell(Denomination cellDenomination, Integer amount) {
    int currentValue = cells.getOrDefault(cellDenomination, 0);
    int newValue = currentValue + amount;
    cells.put(cellDenomination, newValue);
  }

  public void extractFromCell(Denomination cellDenomination, Integer amount) {
    int currentValue = cells.getOrDefault(cellDenomination, 0);
    if (currentValue < amount) {
      throw new RuntimeException("The requested amount %s is greater than available in the cell. Available: %s"
          .formatted(amount, currentValue));
    }

    int newValue = currentValue - amount;
    cells.put(cellDenomination, newValue);
  }

  public int getCellValue(Denomination cellDenomination) {
    return cells.getOrDefault(cellDenomination, 0);
  }

  public Set<Denomination> getCellDenominations() {
    return cells.keySet();
  }
}
