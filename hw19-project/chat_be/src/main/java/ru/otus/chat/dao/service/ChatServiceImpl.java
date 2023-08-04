package ru.otus.chat.dao.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.chat.dao.entity.Chat;
import ru.otus.chat.dao.repository.ChatRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

  private final ChatRepository chatRepository;
  private final UserService userService;

  @Override
  public Long startChat(Long firstUserId, Long secondUserId) {
    var chat = chatRepository.findChatByFirstUser_IdAndSecondUser_Id(firstUserId, secondUserId);
    if (chat.isPresent()) {
      return chat.get().getId();
    }
    chat = chatRepository.findChatByFirstUser_IdAndSecondUser_Id(secondUserId, firstUserId);
    if (chat.isPresent()) {
      return chat.get().getId();
    }

    var firstUser = userService.getUserById(firstUserId);
    var secondUser = userService.getUserById(secondUserId);

    var newChat = Chat.builder()
      .firstUser(firstUser)
      .secondUser(secondUser)
      .build();
    return chatRepository.save(newChat).getId();
  }

  @Override
  public Chat getChatById(Long chatId) {
    return chatRepository.getReferenceById(chatId);
  }
}
