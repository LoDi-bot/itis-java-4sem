package ru.itis.chat.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.chat.models.ChatRoom;
import ru.itis.chat.models.User;

import java.util.List;

public interface ChatRepository extends JpaRepository<ChatRoom, Long>{
    List<ChatRoom> findAllByParticipantsContaining(User user);
}
