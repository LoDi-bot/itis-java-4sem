package ru.itis.stogram.services;

import ru.itis.stogram.dto.ChatRoomDto;
import ru.itis.stogram.dto.CreateChatForm;

import java.util.List;

public interface ChatRoomService {
    List<ChatRoomDto> getAllChats(Long accountId);
    ChatRoomDto createChatRoom(CreateChatForm createChatForm, Long accountId);
    ChatRoomDto getChatRoom(Long chatId, Long accountId);
    void deleteChatRoom(Long chatId, Long accountId);
}
