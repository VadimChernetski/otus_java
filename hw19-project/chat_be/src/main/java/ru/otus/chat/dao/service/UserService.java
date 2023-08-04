package ru.otus.chat.dao.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.otus.chat.dao.entity.User;
import ru.otus.chat.dto.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {

  User getUserById(Long id);
  UserDto findUserByEmail(String email);

  void saveUser(String nickname, String email, String password);

  List<UserDto> getAllByIdNotEqualsToGiven(Long id);


}
