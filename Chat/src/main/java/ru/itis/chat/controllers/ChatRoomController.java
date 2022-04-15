package ru.itis.chat.controllers;

import com.auth0.jwt.JWT;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.itis.chat.dto.ChatRoomDto;
import ru.itis.chat.dto.CreateChatForm;
import ru.itis.chat.services.ChatRoomService;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/chats")
public class ChatRoomController {
    private final ChatRoomService chatRoomsService;

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping
    public ResponseEntity<List<ChatRoomDto>> getAllChats(Authentication authentication) {
        Long accountId = Long.valueOf(JWT.decode(authentication.getPrincipal().toString()).getSubject());
        return ResponseEntity
                .ok()
                .body(chatRoomsService.getAllChats(accountId));

    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping
    public ResponseEntity<ChatRoomDto> createChatRoom(@RequestBody CreateChatForm createChatForm, Authentication authentication) {
        Long accountId = Long.valueOf(JWT.decode(authentication.getPrincipal().toString()).getSubject());
        return ResponseEntity
                .ok()
                .body(chatRoomsService.createChatRoom(createChatForm, accountId));

    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/{id}")
    public ResponseEntity<ChatRoomDto> getChatRoom(@PathVariable("id") Long chatRoomId, Authentication authentication) {
        Long accountId = Long.valueOf(JWT.decode(authentication.getPrincipal().toString()).getSubject());
        return ResponseEntity
                .ok()
                .body(chatRoomsService.getChatRoom(chatRoomId, accountId));

    }

    @PreAuthorize("hasAuthority('USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteChatRoom(@PathVariable("id") Long chatRoomId, Authentication authentication) {
        Long accountId = Long.valueOf(JWT.decode(authentication.getPrincipal().toString()).getSubject());
        chatRoomsService.deleteChatRoom(chatRoomId, accountId);
        return ResponseEntity.ok().build();
    }

}

