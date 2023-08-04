package ru.otus.chat.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.otus.chat.dao.entity.User;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

  @Override
  public User getLoggedInUser() {
    final Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (principal instanceof User user) {
      return user;
    }
    throw new RuntimeException();
  }
}
