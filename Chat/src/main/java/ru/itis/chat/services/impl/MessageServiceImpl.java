package ru.itis.chat.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itis.chat.dto.ChatRoomDto;
import ru.itis.chat.dto.CreateMessageForm;
import ru.itis.chat.dto.MessageDto;
import ru.itis.chat.models.Message;
import ru.itis.chat.repositories.ChatRepository;
import ru.itis.chat.repositories.MessageRepository;
import ru.itis.chat.repositories.UsersRepository;
import ru.itis.chat.services.MessageService;

@RequiredArgsConstructor
@Service
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    private final UsersRepository usersRepository;
    private final ChatRepository chatRepository;

    @Override
    public ChatRoomDto sendMessage(CreateMessageForm createMessageForm, Long authorId) {
        Message message = Message.builder()
                .author(usersRepository.findById(authorId).get())
                .chat(chatRepository.findById(createMessageForm.getChatRoomId()).get())
                .body(createMessageForm.getBody())
                .build();
        messageRepository.save(message);
        return ChatRoomDto.from(chatRepository.findById(createMessageForm.getChatRoomId()).get());
    }

    @Override
    public ChatRoomDto editMessage(String newText, Long messageId) {
        Message message = messageRepository.findById(messageId).get();
        message.setBody(newText);
        messageRepository.save(message);
        return ChatRoomDto.from(chatRepository.findById(message.getChat().getId()).get());
    }

    @Override
    public void deleteMessage(Long messageId) {
        messageRepository.deleteById(messageId);
    }
}
