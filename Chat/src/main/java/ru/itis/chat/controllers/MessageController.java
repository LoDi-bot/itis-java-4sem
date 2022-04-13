package ru.itis.chat.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.itis.chat.dto.ChatRoomDto;
import ru.itis.chat.dto.CreateMessageForm;
import ru.itis.chat.dto.MessageDto;
import ru.itis.chat.services.MessageService;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
@RequestMapping("/message")
public class MessageController {

    private final MessageService messageService;

    @PostMapping
    public ResponseEntity<ChatRoomDto> sendMessage(@RequestBody CreateMessageForm createMessageForm, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .build();
        } else {
            return ResponseEntity
                    .ok()
                    .body(messageService.sendMessage(createMessageForm, userId));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ChatRoomDto> editMessage(@RequestBody MessageDto messageDto, @PathVariable("id") Long id) {
        return ResponseEntity
                .ok()
                .body(messageService.editMessage(messageDto.getBody(), id));
    }

    @DeleteMapping("/{id}")
    public void deleteMessage(@PathVariable("id") Long messageId) {
        messageService.deleteMessage(messageId);
    }
}
