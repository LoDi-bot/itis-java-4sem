package ru.itis.chat.dto;

import lombok.*;
import ru.itis.chat.models.ChatRoom;
import ru.itis.chat.models.Message;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatRoomDto {

    private Long id;

    private List<UserDto> participants;

    private List<Message> messages;

    private ChatRoom.Type type;

    public static ChatRoomDto from(ChatRoom chatRoom) {
        return ChatRoomDto.builder()
                .id(chatRoom.getId())
                .participants(chatRoom.getParticipants().stream().map(UserDto::from).collect(Collectors.toList()))
                .messages(chatRoom.getMessages())
                .type(chatRoom.getType())
                .build();
    }
}
