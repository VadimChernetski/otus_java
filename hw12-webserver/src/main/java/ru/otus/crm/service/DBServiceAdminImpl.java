package ru.otus.crm.service;

import ru.otus.core.repository.DataTemplate;
import ru.otus.core.sessionmanager.TransactionManager;
import ru.otus.crm.model.Admin;

import java.util.Optional;

public class DBServiceAdminImpl implements DBServiceAdmin {

  private final DataTemplate<Admin> adminDataTemplate;
  private final TransactionManager transactionManager;

  public DBServiceAdminImpl(TransactionManager transactionManager, DataTemplate<Admin> adminDataTemplate) {
    this.transactionManager = transactionManager;
    this.adminDataTemplate = adminDataTemplate;
  }

  @Override
  public Optional<Admin> findByEmail(String email) {
    return transactionManager.doInReadOnlyTransaction(session -> {
      var admins =  adminDataTemplate.findByEntityField(session, "email", email);
      if (admins.size() < 2) {
        return admins.stream().findFirst();
      } else {
        throw new RuntimeException("Illegal amount of admins with the same email");
      }
    });
  }
}
