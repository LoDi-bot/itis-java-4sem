package ru.itis.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.chat.models.Message;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageDto {
    private Long id;
    private String body;
    private UserDto author;
    private Long chatRoomId;

    public static MessageDto from(Message message) {
        return MessageDto.builder()
                .id(message.getId())
                .body(message.getBody())
                .author(UserDto.from(message.getAuthor()))
                .chatRoomId(message.getChat().getId())
                .build();
    }
}
