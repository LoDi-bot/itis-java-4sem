package ru.itis.chat.controllers;

import com.auth0.jwt.JWT;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.itis.chat.dto.ChatRoomDto;
import ru.itis.chat.dto.CreateMessageForm;
import ru.itis.chat.dto.MessageDto;
import ru.itis.chat.services.MessageService;

@RequiredArgsConstructor
@Controller
@RequestMapping("/message")
public class MessageController {

    private final MessageService messageService;

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping
    public ResponseEntity<ChatRoomDto> sendMessage(@RequestBody CreateMessageForm createMessageForm, Authentication authentication) {
        Long accountId = Long.valueOf(JWT.decode(authentication.getPrincipal().toString()).getSubject());
        return ResponseEntity
                .ok()
                .body(messageService.sendMessage(createMessageForm, accountId));
    }

    @PreAuthorize("hasAuthority('USER')")
    @PutMapping
    public ResponseEntity<ChatRoomDto> editMessage(@RequestBody MessageDto messageDto, Authentication authentication) {
        Long accountId = Long.valueOf(JWT.decode(authentication.getPrincipal().toString()).getSubject());
        return ResponseEntity
                .ok()
                .body(messageService.editMessage(messageDto, accountId));
    }

    @PreAuthorize("hasAuthority('USER')")
    @DeleteMapping("/{id}")
    public void deleteMessage(@PathVariable("id") Long messageId, Authentication authentication) {
        Long accountId = Long.valueOf(JWT.decode(authentication.getPrincipal().toString()).getSubject());
        messageService.deleteMessage(messageId, accountId);
    }
}
