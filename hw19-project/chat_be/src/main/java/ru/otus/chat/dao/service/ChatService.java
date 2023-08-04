package ru.otus.chat.dao.service;

import ru.otus.chat.dao.entity.Chat;

public interface ChatService {

  Long startChat(Long firstUserId, Long secondUserId);
  Chat getChatById(Long chatId);
}
