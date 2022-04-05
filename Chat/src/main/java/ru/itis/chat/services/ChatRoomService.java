package ru.itis.chat.services;

import ru.itis.chat.dto.ChatRoomDto;
import ru.itis.chat.dto.CreateChatForm;

import java.util.List;

public interface ChatRoomService {
    List<ChatRoomDto> getAllChats(Long userId);
    ChatRoomDto createChatRoom(CreateChatForm createChatForm, Long userId);
    ChatRoomDto getChatRoom(Long id);
    void deleteChatRoom(Long id);
}
