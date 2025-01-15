package ru.atm.service.impl;

import ru.atm.Denomination;
import ru.atm.service.Dispenser;
import ru.atm.service.Storage;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class MinimalBanknotesDispenserImpl implements Dispenser {

  private final Storage storage;

  public MinimalBanknotesDispenserImpl(Storage storage) {
    this.storage = storage;
  }

  @Override
  public Map<Denomination, Integer> dispense(int requestedAmount) {
    var cells = storage.getCellDenominations();
    if (cells.isEmpty()) {
      throw new RuntimeException("The storage is empty");
    }

    var sortedCells = sortDenominationsDesc(cells);
    Map<Denomination, Integer> result = getRequestedAmountWithMinimumNumberOfBanknotes(requestedAmount, sortedCells);

    validateResult(requestedAmount, result);
    return result;
  }

  private Map<Denomination, Integer> getRequestedAmountWithMinimumNumberOfBanknotes(
      int requestedAmount, Set<Denomination> sortedCells) {
    Map<Denomination, Integer> result = new TreeMap<>();
    for (var cellDenomination : sortedCells) {
      if (requestedAmount / cellDenomination.getValue() > 0) {
        var neededCount = requestedAmount / cellDenomination.getValue();
        var actualCount = getFromCellNotGreaterThan(cellDenomination, neededCount);
        result.put(cellDenomination, actualCount);
        requestedAmount -= cellDenomination.getValue() * actualCount;
      }
    }
    return result;
  }

  private Set<Denomination> sortDenominationsDesc(Set<Denomination> unsorted) {
    Set<Denomination> sorted = new TreeSet<>(Collections.reverseOrder());
    sorted.addAll(unsorted);
    return sorted;
  }

  private int getFromCellNotGreaterThan(Denomination cellDenomination, Integer neededAmount) {
    int currentValue = storage.getCellValue(cellDenomination);
    if (currentValue >= neededAmount) {
      storage.extractFromCell(cellDenomination, neededAmount);
      return neededAmount;
    } else {
      storage.extractFromCell(cellDenomination, currentValue);
      return currentValue;
    }
  }

  private void validateResult(int requestedAmount, Map<Denomination, Integer> result) {
    int resultAmount = 0;
    for (var entry : result.entrySet()) {
      resultAmount += entry.getKey().getValue() * entry.getValue();
    }

    if (resultAmount != requestedAmount) {
      throw new RuntimeException("The requested amount can not be issued");
    }
  }
}
