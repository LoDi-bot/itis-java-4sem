package ru.itis.chat.services;

import ru.itis.chat.dto.ChatRoomDto;
import ru.itis.chat.dto.CreateChatForm;

import java.util.List;

public interface ChatRoomService {
    List<ChatRoomDto> getAllChats(Long accountId);
    ChatRoomDto createChatRoom(CreateChatForm createChatForm, Long accountId);
    ChatRoomDto getChatRoom(Long chatId, Long accountId);
    void deleteChatRoom(Long chatId, Long accountId);
}
