package ru.itis.chat.services;

import ru.itis.chat.dto.ChatRoomDto;
import ru.itis.chat.dto.CreateMessageForm;
import ru.itis.chat.dto.MessageDto;

public interface MessageService {
    ChatRoomDto sendMessage(CreateMessageForm createMessageForm, Long authorId);
    ChatRoomDto editMessage(MessageDto messageDto, Long accountId);

    void deleteMessage(Long messageId, Long accountId);
}
