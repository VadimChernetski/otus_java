package ru.otus.chat.dao.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.chat.dao.entity.Message;
import ru.otus.chat.dao.repository.MessageRepository;
import ru.otus.chat.dto.MessageDto;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

  private final MessageRepository messageRepository;
  private final UserService userService;
  private final ChatService chatService;

  @Override
  public void saveMessage(MessageDto messageDto) {
    var user = userService.getUserById(messageDto.getAuthorId());
    var chat = chatService.getChatById(messageDto.getChatId());
    var message = Message.builder()
      .author(user)
      .chat(chat)
      .text(messageDto.getText())
      .createdTimestamp(Instant.now())
      .build();

    messageRepository.save(message);
  }

  @Override
  public List<MessageDto> getChatMessages(Long chatId) {
    return messageRepository.getAllByChat_id(chatId).stream()
      .sorted(Comparator.comparing(Message::getCreatedTimestamp))
      .map(message -> MessageDto.builder()
        .authorId(message.getAuthor().getId())
        .chatId(message.getChat().getId())
        .text(message.getText())
        .build())
      .toList();
  }
}
