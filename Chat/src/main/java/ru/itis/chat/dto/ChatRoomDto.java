package ru.itis.chat.dto;

import lombok.*;
import ru.itis.chat.models.ChatRoom;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatRoomDto {

    private Long id;

    private Set<UserDto> participants;

    private List<MessageDto> messages;

    public static ChatRoomDto from(ChatRoom chatRoom) {
        return ChatRoomDto.builder()
                .id(chatRoom.getId())
                .participants(chatRoom.getParticipants().stream().map(UserDto::from).collect(Collectors.toSet()))
                .messages(chatRoom.getMessages().stream().map(MessageDto::from).collect(Collectors.toList()))
                .build();
    }
}
