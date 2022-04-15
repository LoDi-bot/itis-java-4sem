package ru.itis.chat.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.itis.chat.dto.MessageDto;
import ru.itis.chat.models.Message;

@RequiredArgsConstructor
@Component
public class MessagesMapper {
    private final AccountsMapper accountsMapper;

    public MessageDto toMessageDto(Message message) {
        return MessageDto.builder()
                .id(message.getId())
                .body(message.getBody())
                .author(accountsMapper.toAccountDto(message.getAuthor()))
                .chatRoomId(message.getChat().getId())
                .build();
    }
}
