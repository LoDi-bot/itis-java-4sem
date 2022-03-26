package ru.itis.chat.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.itis.chat.dto.ChatRoomDto;
import ru.itis.chat.services.ChatRoomService;

@RequiredArgsConstructor
@Controller
@RequestMapping("/chat")
public class ChatRoomController {
    private final ChatRoomService chatRoomsService;

    @PostMapping("/new")
    public String createChatRoom(@RequestBody ChatRoomDto chatRoomDto) {
        chatRoomDto = chatRoomsService.createChatRoom(chatRoomDto);
        return String.format("redirect:/chat/{%d}", chatRoomDto.getId());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChatRoomDto> getChatRoom(@PathVariable("id") Long chatRoomId) {
        return ResponseEntity.ok(chatRoomsService.getChatRoom(chatRoomId));
    }

    @DeleteMapping("/{id}")
    public void deleteChatRoom(@PathVariable("id") Long chatRoomId) {
        chatRoomsService.deleteChatRoom(chatRoomId);
    }

}

