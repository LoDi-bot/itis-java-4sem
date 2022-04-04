package ru.itis.chat.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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
    public void sendMessage(MessageDto messageDto) {
        Message message = Message.builder()
                .author(usersRepository.findById(messageDto.getAuthor().getId()).get())
                .chat(chatRepository.findById(messageDto.getChatRoomId()).get())
                .body(messageDto.getBody())
                .build();
        messageRepository.save(message);
    }

    @Override
    public MessageDto editMessage(String newText, Long messageId) {
        Message message = messageRepository.findById(messageId).get();
        message.setBody(newText);
        return MessageDto.from(messageRepository.save(message));
    }

    @Override
    public void deleteMessage(Long messageId) {
        messageRepository.deleteById(messageId);
    }
}
