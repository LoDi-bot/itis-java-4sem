package ru.itis.chat.services;

import ru.itis.chat.dto.ChatRoomDto;

public interface ChatRoomService {
    ChatRoomDto createChatRoom(ChatRoomDto chatRoomDto);
    ChatRoomDto getChatRoom(Long id);
    void deleteChatRoom(Long id);
}
