package ru.atm.service;

import ru.atm.Denomination;

import java.util.Map;

public interface Atm {

  void acceptBanknotes(Denomination denomination, Integer amount);

  Map<Denomination, Integer> giveOutRequestedAmount(int requestedAmount);

  int getAccountBalance();
}
