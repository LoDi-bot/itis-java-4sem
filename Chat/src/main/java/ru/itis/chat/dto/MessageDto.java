package ru.itis.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageDto {
    @NotNull(message = "MESSAGE_ID_EMPTY")
    private Long id;
    @NotBlank(message = "BLANK_MESSAGE_BODY")
    private String body;
    @NotNull(message = "MESSAGE_AUTHOR_EMPTY")
    private AccountDto author;
    @NotNull(message = "MESSAGE_CHAT_EMPTY")
    private Long chatRoomId;
}
