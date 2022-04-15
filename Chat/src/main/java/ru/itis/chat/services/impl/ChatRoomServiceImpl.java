package ru.itis.chat.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itis.chat.dto.ChatRoomDto;
import ru.itis.chat.dto.CreateChatForm;
import ru.itis.chat.exceptions.ErrorEntity;
import ru.itis.chat.exceptions.ValidationException;
import ru.itis.chat.mapper.ChatRoomsMapper;
import ru.itis.chat.models.ChatRoom;
import ru.itis.chat.models.Account;
import ru.itis.chat.repositories.ChatRepository;
import ru.itis.chat.repositories.AccountsRepository;
import ru.itis.chat.services.ChatRoomService;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

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
    public ChatRoomDto createChatRoom(CreateChatForm createChatForm, Long accountId) {
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
