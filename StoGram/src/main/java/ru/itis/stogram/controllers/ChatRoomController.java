package ru.itis.stogram.controllers;

import com.auth0.jwt.JWT;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.itis.stogram.dto.ChatRoomDto;
import ru.itis.stogram.dto.CreateChatForm;
import ru.itis.stogram.services.ChatRoomService;

import javax.validation.Valid;
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
    public ResponseEntity<ChatRoomDto> createChatRoom(@Valid @RequestBody CreateChatForm createChatForm, Authentication authentication) {
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

