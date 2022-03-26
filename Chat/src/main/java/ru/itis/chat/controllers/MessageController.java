package ru.itis.chat.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.itis.chat.dto.MessageDto;
import ru.itis.chat.services.MessageService;

@RequiredArgsConstructor
@Controller
@RequestMapping("/message")
public class MessageController {

    private final MessageService messageService;

    @PostMapping
    public void sendMessage(@RequestBody MessageDto messageDto) {
        messageService.sendMessage(messageDto);
    }

    @PutMapping("/{id}")
    public MessageDto editMessage(@RequestBody String newText, @PathVariable("id") Long messageId) {
        return messageService.editMessage(newText, messageId);
    }

    @DeleteMapping("/{id}")
    public void editMessage(@PathVariable("id") Long messageId) {
        messageService.deleteMessage(messageId);
    }
}
