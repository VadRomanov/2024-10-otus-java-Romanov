package ru.atm;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.atm.service.Atm;
import ru.atm.service.Storage;
import ru.atm.service.impl.AtmImpl;
import ru.atm.service.impl.BalanceCounterImpl;
import ru.atm.service.impl.MinimalBanknotesDispenserImpl;
import ru.atm.service.impl.SaverImpl;
import ru.atm.service.impl.StorageImpl;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AtmTest {
  private Atm atm;
  private Storage storage;

  @ParameterizedTest()
  @ValueSource(ints = {0, -1})
  void giveOutRequestedAmountZeroOrNegativeValueTest(int value) {
    this.atm = new AtmImpl(null, null, null);
    RuntimeException exception = assertThrows(RuntimeException.class, () -> atm.giveOutRequestedAmount(value));

    assertEquals("Requested amount can not be less then 0", exception.getMessage());
  }

  @Test
  void giveOutRequestedAmountMoreThanBalanceTest() {
    this.atm = new AtmImpl(null, null, new BalanceCounterImpl(new StorageImpl()));
    RuntimeException exception = assertThrows(RuntimeException.class, () -> atm.giveOutRequestedAmount(1_000_000));

    assertEquals("Requested amount is greater then account balance", exception.getMessage());
  }

  @Test
  void giveOutRequestedAmountTest() {
    storage = new StorageImpl();
    this.atm = new AtmImpl(new SaverImpl(storage), new MinimalBanknotesDispenserImpl(storage), new BalanceCounterImpl(storage));

    atm.acceptBanknotes(Denomination.FIVE_HUNDRED, 2);
    atm.acceptBanknotes(Denomination.ONE_HUNDRED, 12);
    atm.acceptBanknotes(Denomination.ONE, 7);

    var result = atm.giveOutRequestedAmount(1207);

    assertAll(
        () -> assertEquals(3, result.size()),
        () -> assertEquals(2, result.get(Denomination.FIVE_HUNDRED)),
        () -> assertEquals(2, result.get(Denomination.ONE_HUNDRED)),
        () -> assertEquals(7, result.get(Denomination.ONE))
    );
  }

  @DisplayName("вернуть положительный баланс на счете")
  @Test
  void displayAccountBalanceTest() {
    storage = mock(Storage.class);
    this.atm = new AtmImpl(null, null, new BalanceCounterImpl(storage));

    when(storage.getCellValue(Denomination.FIVE_HUNDRED)).thenReturn(2);
    when(storage.getCellValue(Denomination.ONE_HUNDRED)).thenReturn(2);
    when(storage.getCellValue(Denomination.ONE)).thenReturn(7);
    when(storage.getCellDenominations()).thenReturn(Set.of(Denomination.FIVE_HUNDRED, Denomination.ONE_HUNDRED, Denomination.ONE));

    var result = atm.getAccountBalance();

    assertEquals(1207, result);
  }

  @DisplayName("пополнить счет")
  @Test
  void acceptBanknotesTest() {
    storage = new StorageImpl();
    this.atm = new AtmImpl(new SaverImpl(storage), null, new BalanceCounterImpl(storage));

    atm.acceptBanknotes(Denomination.FIVE_HUNDRED, 2);
    atm.acceptBanknotes(Denomination.ONE_HUNDRED, 2);
    atm.acceptBanknotes(Denomination.ONE, 7);

    assertEquals(1207, atm.getAccountBalance());
  }

}