package ru.itis.stogram.controllers;

import com.auth0.jwt.JWT;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.itis.stogram.dto.ChatRoomDto;
import ru.itis.stogram.dto.CreateMessageForm;
import ru.itis.stogram.dto.MessageDto;
import ru.itis.stogram.services.MessageService;

import javax.validation.Valid;

@RequiredArgsConstructor
@Controller
@RequestMapping("/message")
public class MessageController {

    private final MessageService messageService;

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping
    public ResponseEntity<ChatRoomDto> sendMessage(@Valid @RequestBody CreateMessageForm createMessageForm, Authentication authentication) {
        Long accountId = Long.valueOf(JWT.decode(authentication.getPrincipal().toString()).getSubject());
        return ResponseEntity
                .ok()
                .body(messageService.sendMessage(createMessageForm, accountId));
    }

    @PreAuthorize("hasAuthority('USER')")
    @PutMapping
    public ResponseEntity<ChatRoomDto> editMessage(@Valid @RequestBody MessageDto messageDto, Authentication authentication) {
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
