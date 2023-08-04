package ru.otus.chat.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.chat.dao.entity.Chat;

import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

  Optional<Chat> findChatByFirstUser_IdAndSecondUser_Id(Long firstUserId, Long secondUserId);

}
