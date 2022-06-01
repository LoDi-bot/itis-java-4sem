package ru.itis.stogram.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.stogram.models.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
