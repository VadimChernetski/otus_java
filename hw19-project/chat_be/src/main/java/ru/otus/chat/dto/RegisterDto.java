package ru.otus.chat.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class RegisterDto {

  private String nickname;
  private String email;
  private String password;

}
