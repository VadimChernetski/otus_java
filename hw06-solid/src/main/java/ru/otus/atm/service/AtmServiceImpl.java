package ru.otus.atm.service;

import ru.otus.atm.exception.AtmException;
import ru.otus.atm.model.Account;
import ru.otus.atm.model.Atm;
import ru.otus.atm.model.Banknote;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public class AtmServiceImpl implements AtmService {

  private final Integer ONE_BANKNOTE = 1;

  private final Atm atm;

  public AtmServiceImpl(Atm atm) {
    this.atm = atm;
  }

  @Override
  public Map<Banknote, Integer> getAvailableBanknotes() {
    return withAuthenticatedAccount(atm::getBanknotes);
  }

  @Override
  public void deposit(Banknote banknote) {
    withAuthenticatedAccount(() -> {
      if (banknote == null) {
        throw new AtmException("invalid banknote");
      }
      var newAmount = atm.getBanknotes()
        .computeIfPresent(banknote, (givenBanknote, amount) -> amount + 1);
      if (newAmount == null) {
        atm.getBanknotes().computeIfAbsent(banknote, (givenBanknote) -> ONE_BANKNOTE);
      }
    });
  }

  @Override
  public Map<Banknote, Integer> withdraw(int amount) {
    return withAuthenticatedAccount(() -> {
      if (amount > atm.getAuthenticatedAccount().balance()) {
        throw new AtmException("low balance");
      }
      var result = new HashMap<Banknote, Integer>();
      var amountRef = new AtomicReference<>(amount);
      atm.getBanknotes().forEach(((banknote, amountOfBanknotesInAtm) -> {
        if (amountOfBanknotesInAtm != null && amountOfBanknotesInAtm != 0) {
          var amountOfBanknotes = amountRef.get() / banknote.getValue() > amountOfBanknotesInAtm ?
            amountOfBanknotesInAtm : amountRef.get() / banknote.getValue();
          if (amountOfBanknotes > 0) {
            result.put(banknote, amountOfBanknotes);
            amountRef.set(amountRef.get() % (amountOfBanknotes * banknote.getValue()));
          }
        }
      }));
      if (amountRef.get() != 0) {
        throw new AtmException("amount can't be handled");
      }
      removeWithdrawBanknotes(result);
      return result;
    });
  }

  private void removeWithdrawBanknotes(HashMap<Banknote, Integer> result) {
    result.forEach((banknote, amountOfBanknotes) -> atm.getBanknotes()
      .computeIfPresent(banknote,
        ((banknoteInAtm, amountOfBanknotesInAtm) -> amountOfBanknotesInAtm - amountOfBanknotes)));
  }

  @Override
  public boolean authenticate(String password) {
    if (password == null || password.isEmpty()) {
      throw new AtmException("invalid password");
    }
    var account = atm.getAccounts().get(password);
    atm.setAuthenticatedAccount(account);
    return account != null;
  }

  @Override
  public void endSession() {
    withAuthenticatedAccount(() -> atm.setAuthenticatedAccount(null));
  }

  @Override
  public Account getCurrentAccount() {
    return atm.getAuthenticatedAccount();
  }

  private <R> R withAuthenticatedAccount(Supplier<R> supplier) {
    if (atm.getAuthenticatedAccount() != null) {
      return supplier.get();
    } else {
      throw new AtmException("not authenticated");
    }
  }

  private void withAuthenticatedAccount(Action action) {
    if (atm.getAuthenticatedAccount() != null) {
      action.act();
    } else {
      throw new AtmException("not authenticated");
    }
  }

}
