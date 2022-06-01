package ru.itis.stogram.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.itis.stogram.dto.ChatRoomDto;
import ru.itis.stogram.models.ChatRoom;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class ChatRoomsMapper {
    private final AccountsMapper accountsMapper;
    private final MessagesMapper messagesMapper;

    public ChatRoomDto toChatRoomDto(ChatRoom chatRoom) {
        return ChatRoomDto.builder()
                .id(chatRoom.getId())
                .participants(chatRoom.getParticipants().stream().map(accountsMapper::toAccountDto).collect(Collectors.toSet()))
                .messages(chatRoom.getMessages().stream().map(messagesMapper::toMessageDto).collect(Collectors.toList()))
                .build();
    }
}
