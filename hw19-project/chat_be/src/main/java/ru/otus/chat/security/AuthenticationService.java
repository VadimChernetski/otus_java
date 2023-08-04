package ru.otus.chat.security;

import ru.otus.chat.dao.entity.User;

public interface AuthenticationService {

  User getLoggedInUser();
}
