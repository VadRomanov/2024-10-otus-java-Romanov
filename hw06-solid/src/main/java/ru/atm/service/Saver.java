package ru.atm.service;

import ru.atm.Denomination;

public interface Saver {
  void save(Denomination denomination, Integer amount);
}
