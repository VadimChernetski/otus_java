package ru.otus.crm.service;

import ru.otus.crm.model.Admin;

import java.util.Optional;

public interface DBServiceAdmin {

  Optional<Admin> findByEmail(String  email);

}
