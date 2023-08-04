package ru.otus.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import ru.otus.chat.dao.service.MessageService;
import ru.otus.chat.dto.MessageDto;

@Controller
@RequiredArgsConstructor
public class WsMessageController {

  private final MessageService messageService;

  @MessageMapping("/message.{chatId}")
  @SendTo("/topic/message.{chatId}")
  public MessageDto getMessage(MessageDto messageDto) {
    messageService.saveMessage(messageDto);
    return messageDto;
  }

}
