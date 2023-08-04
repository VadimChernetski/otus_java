package ru.otus.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.chat.dao.entity.User;
import ru.otus.chat.dao.service.UserService;
import ru.otus.chat.dto.RegisterDto;
import ru.otus.chat.dto.UserDto;
import ru.otus.chat.security.AuthenticationService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

  private final AuthenticationService authenticationService;
  private final UserService userService;
  private final PasswordEncoder passwordEncoder;

  @PostMapping("/register")
  public ResponseEntity<Void> register(@RequestBody RegisterDto registerDto) {
    var password = passwordEncoder.encode(registerDto.getPassword());
    userService.saveUser(registerDto.getNickname(), registerDto.getEmail(), password);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/user-info")
  public UserDto getUserInfo() {
    var user = authenticationService.getLoggedInUser();
    return userService.findUserByEmail(user.getEmail());
  }

  @GetMapping("/user")
  public List<UserDto> getAllUsers() {
    var user = authenticationService.getLoggedInUser();
    return userService.getAllByIdNotEqualsToGiven(user.getId());
  }

}
