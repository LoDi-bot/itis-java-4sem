package ru.itis.stogram.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.stogram.models.Account;
import ru.itis.stogram.models.ChatRoom;

import java.util.List;

public interface ChatRepository extends JpaRepository<ChatRoom, Long>{
    List<ChatRoom> findAllByParticipantsContaining(Account account);
}
