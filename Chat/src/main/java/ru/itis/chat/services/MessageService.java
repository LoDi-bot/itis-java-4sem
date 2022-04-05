package ru.itis.chat.services;

import ru.itis.chat.dto.ChatRoomDto;
import ru.itis.chat.dto.CreateMessageForm;

public interface MessageService {
    ChatRoomDto sendMessage(CreateMessageForm createMessageForm, Long authorId);
    ChatRoomDto editMessage(String newText, Long messageId);

    void deleteMessage(Long messageId);
}
