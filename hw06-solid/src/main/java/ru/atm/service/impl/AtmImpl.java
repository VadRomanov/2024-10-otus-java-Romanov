package ru.atm.service.impl;

import ru.atm.Denomination;
import ru.atm.service.Atm;
import ru.atm.service.BalanceCounter;
import ru.atm.service.Dispenser;
import ru.atm.service.Saver;

import java.util.Map;

public class AtmImpl implements Atm {

  private final Saver saver;
  private final Dispenser dispenser;
  private final BalanceCounter balanceCounter;

  public AtmImpl(Saver saver, Dispenser dispenser, BalanceCounter balanceCounter) {
    this.saver = saver;
    this.dispenser = dispenser;
    this.balanceCounter = balanceCounter;
  }

  @Override
  public void acceptBanknotes(Denomination denomination, Integer amount) {
    saver.save(denomination, amount);
  }

  @Override
  public Map<Denomination, Integer> giveOutRequestedAmount(int requestedAmount) {
    if (requestedAmount <= 0) {
      throw new IllegalArgumentException("Requested amount can not be less then 0. Requested: %s".formatted(requestedAmount));
    }
    if (requestedAmount > balanceCounter.countBalance()) {
      throw new IllegalArgumentException("The requested amount is greater then account balance. Requested: %s".formatted(requestedAmount));
    }
    return dispenser.dispense(requestedAmount);
  }

  @Override
  public int getAccountBalance() {
    return balanceCounter.countBalance();
  }
}
