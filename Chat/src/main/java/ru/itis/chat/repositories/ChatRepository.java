package ru.itis.chat.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.chat.models.ChatRoom;

public interface ChatRepository extends JpaRepository<ChatRoom, Long>{
}
