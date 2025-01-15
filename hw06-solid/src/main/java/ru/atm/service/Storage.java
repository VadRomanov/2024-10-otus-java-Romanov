package ru.atm.service;

import ru.atm.Denomination;

import java.util.Set;

public interface Storage {

  void addToCell(Denomination cellDenomination, Integer amount);

  void extractFromCell(Denomination cellDenomination, Integer amount);

  int getCellValue(Denomination cellDenomination);

  Set<Denomination> getCellDenominations();
}
