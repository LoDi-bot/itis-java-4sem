package ru.itis.stogram.services;

import ru.itis.stogram.dto.ChatRoomDto;
import ru.itis.stogram.dto.CreateMessageForm;
import ru.itis.stogram.dto.MessageDto;

public interface MessageService {
    ChatRoomDto sendMessage(CreateMessageForm createMessageForm, Long authorId);
    ChatRoomDto editMessage(MessageDto messageDto, Long accountId);

    void deleteMessage(Long messageId, Long accountId);
}
