package ru.itis.stogram.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.itis.stogram.dto.ChatRoomDto;
import ru.itis.stogram.dto.CreateChatForm;
import ru.itis.stogram.exceptions.ErrorEntity;
import ru.itis.stogram.exceptions.ValidationException;
import ru.itis.stogram.mappers.ChatRoomsMapper;
import ru.itis.stogram.models.Account;
import ru.itis.stogram.models.ChatRoom;
import ru.itis.stogram.repositories.AccountsRepository;
import ru.itis.stogram.repositories.ChatRepository;
import ru.itis.stogram.services.ChatRoomService;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Validated
@RequiredArgsConstructor
@Service
public class ChatRoomServiceImpl implements ChatRoomService {
    private final ChatRepository chatRepository;
    private final AccountsRepository accountsRepository;
    private final ChatRoomsMapper chatRoomsMapper;

    @Override
    public List<ChatRoomDto> getAllChats(Long accountId) {
        return chatRepository.findAllByParticipantsContaining(accountsRepository.getById(accountId))
                .stream()
                .map(chatRoomsMapper::toChatRoomDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public ChatRoomDto createChatRoom(@Valid CreateChatForm createChatForm, Long accountId) {
        Set<Account> participants = new HashSet<>();

        participants.add(accountsRepository
                .findById(accountId)
                .orElseThrow((Supplier<RuntimeException>)
                        () -> new ValidationException(ErrorEntity.ACCOUNT_NOT_FOUND)));
        participants.add(accountsRepository
                .findById(createChatForm.getReceiverId())
                .orElseThrow((Supplier<RuntimeException>)
                        () -> new ValidationException(ErrorEntity.ACCOUNT_NOT_FOUND)));

        ChatRoom chatRoom = ChatRoom.builder()
                .participants(participants)
                .messages(new ArrayList<>())
                .build();

        return chatRoomsMapper.toChatRoomDto(chatRepository.save(chatRoom));
    }

    @Override
    public ChatRoomDto getChatRoom(Long chatId, Long accountId) {
        ChatRoom chatRoom = chatRepository
                .findById(chatId)
                .orElseThrow((Supplier<RuntimeException>)
                        () -> new ValidationException(ErrorEntity.CHAT_NOT_FOUND));
        if (chatRoom.getParticipants().contains(accountsRepository.getById(accountId))) {
            return  chatRoomsMapper.toChatRoomDto(chatRoom);
        } else {
            throw new ValidationException(ErrorEntity.ACCESS_TO_FOREIGN_DATA);
        }
    }

    @Override
    public void deleteChatRoom(Long chatId, Long accountId) {
        ChatRoom chatRoom = chatRepository
                .findById(chatId)
                .orElseThrow((Supplier<RuntimeException>)
                        () -> new ValidationException(ErrorEntity.CHAT_NOT_FOUND));
        if (chatRoom.getParticipants().contains(accountsRepository.getById(accountId))) {
            chatRepository.deleteById(chatId);
        } else {
            throw new ValidationException(ErrorEntity.ACCESS_TO_FOREIGN_DATA);
        }
    }
}
