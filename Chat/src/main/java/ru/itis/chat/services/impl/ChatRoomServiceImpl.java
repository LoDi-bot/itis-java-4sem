package ru.itis.chat.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.itis.chat.dto.ChatRoomDto;
import ru.itis.chat.dto.CreateChatForm;
import ru.itis.chat.models.ChatRoom;
import ru.itis.chat.models.User;
import ru.itis.chat.repositories.ChatRepository;
import ru.itis.chat.repositories.UsersRepository;
import ru.itis.chat.services.ChatRoomService;

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
    private final UsersRepository usersRepository;

    @Override
    public List<ChatRoomDto> getAllChats(Long userId) {
        return chatRepository.findAllByParticipantsContaining(usersRepository.getById(userId))
                .stream()
                .map(ChatRoomDto::from)
                .collect(Collectors.toList());
    }

    @Override
    public ChatRoomDto createChatRoom(CreateChatForm createChatForm, Long userId) {
        Set<User> participants = new HashSet<>();
        participants.add(usersRepository.findById(userId).get());
        participants.add(usersRepository.findById(createChatForm.getReceiverId()).get());
        ChatRoom chatRoom = ChatRoom.builder()
                .participants(participants)
                .messages(new ArrayList<>())
                .build();

        return ChatRoomDto.from(chatRepository.save(chatRoom));
    }

    @Override
    public ChatRoomDto getChatRoom(Long id) {
        return ChatRoomDto.from(chatRepository.findById(id).get());
    }

    @Override
    public void deleteChatRoom(Long id) {
        chatRepository.deleteById(id);
    }
}
