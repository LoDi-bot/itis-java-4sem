package ru.itis.stogram.dto;

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
public class CreateMessageForm {
    @NotBlank(message = "BLANK_MESSAGE_BODY")
    private String body;
    @NotNull
    private Long chatRoomId;
}
