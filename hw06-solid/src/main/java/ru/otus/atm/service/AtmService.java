package ru.otus.atm.service;

import ru.otus.atm.model.Account;
import ru.otus.atm.model.Banknote;

import java.util.Map;

public interface AtmService {

  Map<Banknote, Integer> getAvailableBanknotes();

  void deposit(Banknote banknote);

  Map<Banknote, Integer> withdraw(int amount);

  boolean authenticate(String password);

  void endSession();

  Account getCurrentAccount();

}
