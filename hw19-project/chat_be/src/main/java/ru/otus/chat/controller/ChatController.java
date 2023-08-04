package ru.otus.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.chat.dao.service.ChatService;
import ru.otus.chat.dao.service.MessageService;
import ru.otus.chat.dto.MessageDto;
import ru.otus.chat.security.AuthenticationService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatController {

  private final AuthenticationService authenticationService;
  private final ChatService chatService;
  private final MessageService messageService;

  @GetMapping("/start")
  public Long startChat(@RequestParam("id") Long id) {
    var user = authenticationService.getLoggedInUser();
    return chatService.startChat(id, user.getId());
  }

  @GetMapping("/messages")
  public List<MessageDto> getPreviousMessages(@RequestParam("id") Long id) {
    return messageService.getChatMessages(id);
  }

}
