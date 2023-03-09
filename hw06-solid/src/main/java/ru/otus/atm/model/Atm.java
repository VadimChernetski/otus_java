package ru.otus.atm.model;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class Atm {

  SortedMap<Banknote, Integer> banknotes = new TreeMap<>(Comparator.comparingInt(Banknote::getValue).reversed());
  private final Map<String, Account> accounts = new HashMap<>();
  private Account authenticatedAccount;

  public Map<Banknote, Integer> getBanknotes() {
    return banknotes;
  }

  public Map<String, Account> getAccounts() {
    return accounts;
  }

  public Account getAuthenticatedAccount() {
    return authenticatedAccount;
  }

  public void setAuthenticatedAccount(Account authenticatedAccounts) {
    this.authenticatedAccount = authenticatedAccounts;
  }

}
