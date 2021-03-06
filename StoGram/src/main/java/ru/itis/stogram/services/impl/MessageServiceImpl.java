package ru.itis.stogram.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.itis.stogram.dto.ChatRoomDto;
import ru.itis.stogram.dto.CreateMessageForm;
import ru.itis.stogram.dto.MessageDto;
import ru.itis.stogram.exceptions.ErrorEntity;
import ru.itis.stogram.exceptions.ValidationException;
import ru.itis.stogram.mappers.ChatRoomsMapper;
import ru.itis.stogram.models.Message;
import ru.itis.stogram.repositories.AccountsRepository;
import ru.itis.stogram.repositories.ChatRepository;
import ru.itis.stogram.repositories.MessageRepository;
import ru.itis.stogram.services.MessageService;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;
import java.util.Set;
import java.util.function.Supplier;

@Validated
@RequiredArgsConstructor
@Service
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    private final AccountsRepository accountsRepository;
    private final ChatRepository chatRepository;
    private final ChatRoomsMapper chatRoomsMapper;
    private final Validator validator;

    @Transactional
    @Override
    public ChatRoomDto sendMessage(@Valid CreateMessageForm createMessageForm, Long authorId) {
        Set<ConstraintViolation<CreateMessageForm>> violations = validator.validate(createMessageForm);
        if (!violations.isEmpty()) {
            throw new ValidationException(violations.stream().findFirst().get().getMessage());
        }

        Message message = Message.builder()
                .author(accountsRepository
                        .findById(authorId)
                        .orElseThrow((Supplier<RuntimeException>)
                                () -> new ValidationException(ErrorEntity.ACCOUNT_NOT_FOUND)))
                .chat(chatRepository
                        .findById(createMessageForm.getChatRoomId())
                        .orElseThrow((Supplier<RuntimeException>)
                                () -> new ValidationException(ErrorEntity.CHAT_NOT_FOUND)))
                .body(createMessageForm.getBody())
                .build();

        messageRepository.save(message);

        //noinspection OptionalGetWithoutIsPresent
        return chatRoomsMapper.toChatRoomDto(chatRepository.findById(createMessageForm.getChatRoomId()).get());
    }

    @Transactional
    @Override
    public ChatRoomDto editMessage(@Valid MessageDto messageDto, Long accountId) {
        Set<ConstraintViolation<MessageDto>> violations = validator.validate(messageDto);
        if (!violations.isEmpty()) {
            throw new ValidationException(violations.stream().findFirst().get().getMessage());
        }

        Message message = messageRepository
                .findById(messageDto.getId())
                .orElseThrow((Supplier<RuntimeException>)
                        () -> new ValidationException(ErrorEntity.MESSAGE_NOT_FOUND));

        if (message.getAuthor().equals(accountsRepository.getById(accountId))) {
            message.setBody(messageDto.getBody());
            messageRepository.save(message);
            //noinspection OptionalGetWithoutIsPresent
            return chatRoomsMapper.toChatRoomDto(chatRepository.findById(message.getChat().getId()).get());
        } else {
            throw new ValidationException(ErrorEntity.ACCESS_TO_FOREIGN_DATA);
        }


    }

    @Override
    public void deleteMessage(Long messageId, Long accountId) {
        Message message = messageRepository
                .findById(messageId)
                .orElseThrow((Supplier<RuntimeException>)
                        () -> new ValidationException(ErrorEntity.MESSAGE_NOT_FOUND));

        if (message.getAuthor().equals(accountsRepository.getById(accountId))) {
            messageRepository.deleteById(messageId);
        } else {
            throw new ValidationException(ErrorEntity.ACCESS_TO_FOREIGN_DATA);
        }
    }
}
