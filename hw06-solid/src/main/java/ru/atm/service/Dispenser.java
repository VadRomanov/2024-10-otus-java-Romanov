package ru.atm.service;

import ru.atm.Denomination;

import java.util.Map;

public interface Dispenser {
  Map<Denomination, Integer> dispense(int requestedAmount);
}
