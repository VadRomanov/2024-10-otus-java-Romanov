package ru.atm;

public enum Denomination {
  ONE(1), TWO(2), FIVE(5),
  TEN(10), TWENTY(20), FIFTY(50),
  ONE_HUNDRED(100), TWO_HUNDRED(200), FIVE_HUNDRED(500),
  ONE_THOUSAND(1000);

  private final int value;

  Denomination(int value) {
    this.value = value;
  }

  public int getValue() {
    return this.value;
  }
}
