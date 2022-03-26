package ru.itis.chat.services;

import ru.itis.chat.dto.MessageDto;

public interface MessageService {
    void sendMessage(MessageDto messageDto);
    MessageDto editMessage(String newText, Long messageId);

    void deleteMessage(Long messageId);
}
