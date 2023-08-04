package ru.otus.chat.dao.service;

import ru.otus.chat.dto.MessageDto;

import java.util.List;

public interface MessageService {

  void saveMessage(MessageDto messageDto);

  List<MessageDto> getChatMessages(Long chatId);

}
