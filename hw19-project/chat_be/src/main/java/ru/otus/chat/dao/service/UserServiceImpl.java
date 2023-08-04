package ru.otus.chat.dao.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.otus.chat.dao.entity.User;
import ru.otus.chat.dao.repository.UserRepository;
import ru.otus.chat.dto.UserDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository.getByEmail(username);
  }

  @Override
  public User getUserById(Long id) {
    return userRepository.getReferenceById(id);
  }

  @Override
  public UserDto findUserByEmail(String email) {
    var userOptional = userRepository.findByEmail(email);
    if (userOptional.isEmpty()) {
     throw new RuntimeException();
    }

    var user = userOptional.get();
    return UserDto.builder()
        .id(user.getId())
        .nickname(user.getNickname())
      .build();
  }

  @Override
  public void saveUser(String nickname, String email, String password) {
    User user = User.builder()
      .email(email)
      .nickname(nickname)
      .password(password)
      .build();
    userRepository.save(user);
  }

  @Override
  public List<UserDto> getAllByIdNotEqualsToGiven(Long id) {
    return userRepository.getAllByIdNot(id).stream()
      .map(user -> UserDto.builder()
        .id(user.getId())
        .nickname(user.getNickname())
        .build())
      .toList();
  }
}
