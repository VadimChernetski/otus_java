package ru.otus.atm.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.atm.exception.AtmException;
import ru.otus.atm.model.Account;
import ru.otus.atm.model.Atm;
import ru.otus.atm.model.Banknote;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ArmServiceImplTest {

  private AtmService atmService;

  @BeforeEach
  void initAtm() {
    var atm = new Atm();
    var account = new Account(20000, "pass");
    Stream.of(Banknote.values()).forEach(banknote -> atm.getBanknotes().put(banknote, 2));
    atm.getAccounts().put(account.password(), account);
    atmService = new AtmServiceImpl(atm);
  }

  @Test
  @DisplayName("try to authenticate with incorrect password")
  void tryToAuthenticateWithIncorrectPassword() {
    assertFalse(atmService.authenticate("pass1"));
  }

  @Test
  @DisplayName("unauthenticated request")
  void unauthenticatedRequest() {
    var atmException = assertThrows(AtmException.class, () -> atmService.getAvailableBanknotes());

    Assertions.assertEquals("not authenticated", atmException.getMessage());
  }

  @Test
  @DisplayName("get available banknotes")
  void getAvailableBanknotes() {
    SortedMap<Banknote, Integer> expectedBanknotes =
      new TreeMap<>(Comparator.comparingInt(Banknote::getValue).reversed());
    Stream.of(Banknote.values()).forEach(banknote -> expectedBanknotes.put(banknote, 2));
    atmService.authenticate("pass");
    final Map<Banknote, Integer> actualBanknotes = atmService.getAvailableBanknotes();
    assertEquals(expectedBanknotes, actualBanknotes);
  }

  @Test
  @DisplayName("get money")
  void getMoney() {
    atmService.authenticate("pass");
    var expectedWithdraw = new HashMap<Banknote, Integer>();
    expectedWithdraw.put(Banknote.TWO_THOUSANDS, 1);
    expectedWithdraw.put(Banknote.ONE_HUNDRED, 1);
    expectedWithdraw.put(Banknote.FIFTY, 1);
    var actualWithdraw = atmService.withdraw(2150);
    assertEquals(expectedWithdraw, actualWithdraw);
  }

  @Test
  @DisplayName("withdraw invalid amount")
  void withdrawInvalidAmount() {
    atmService.authenticate("pass");
    var atmException = assertThrows(AtmException.class, () -> atmService.withdraw(23));

    assertEquals("amount can't be handled", atmException.getMessage());
  }

  @Test
  @DisplayName("try to withdraw more than balance")
  void tryToWithdrawMoreThanBalance() {
    atmService.authenticate("pass");
    var atmException = assertThrows(AtmException.class, () -> atmService.withdraw(100000));

    assertEquals("low balance", atmException.getMessage());
  }

  @Test
  @DisplayName("deposit")
  void deposit() {
    atmService.authenticate("pass");
    SortedMap<Banknote, Integer> expectedBanknotes = new TreeMap<>(Comparator.comparingInt(Banknote::getValue).reversed());
    Stream.of(Banknote.values()).forEach(banknote -> {
      if (banknote.equals(Banknote.FIVE_THOUSANDS)) {
        expectedBanknotes.put(banknote, 3);
      } else {
        expectedBanknotes.put(banknote, 2);
      }
    });
    atmService.deposit(Banknote.FIVE_THOUSANDS);
    var actualBanknotes = atmService.getAvailableBanknotes();
    assertEquals(expectedBanknotes, actualBanknotes);
  }

  @Test
  @DisplayName("deposit invalid banknote")
  void depositInvalidBanknote() {
    atmService.authenticate("pass");
    var atmException = assertThrows(AtmException.class, () -> atmService.deposit(null));

    assertEquals("invalid banknote", atmException.getMessage());
  }

  @Test
  @DisplayName("endSession")
  void endSession() {
    atmService.authenticate("pass");
    atmService.endSession();
    assertNull(atmService.getCurrentAccount());
  }
}
