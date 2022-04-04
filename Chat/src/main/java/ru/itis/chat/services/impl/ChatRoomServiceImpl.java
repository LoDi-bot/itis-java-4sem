package ru.itis.chat.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itis.chat.dto.ChatRoomDto;
import ru.itis.chat.models.ChatRoom;
import ru.itis.chat.repositories.ChatRepository;
import ru.itis.chat.repositories.UsersRepository;
import ru.itis.chat.services.ChatRoomService;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ChatRoomServiceImpl implements ChatRoomService {
    private final ChatRepository chatRepository;
    private final UsersRepository usersRepository;

    @Override
    public ChatRoomDto createChatRoom(ChatRoomDto chatRoomDto) {
        ChatRoom chatRoom = ChatRoom.builder()
                .type(chatRoomDto.getType())
                .participants(chatRoomDto.getParticipants().stream().map(userDto -> {
                            return usersRepository.findById(userDto.getId()).get();
                        })
                        .collect(Collectors.toSet()))
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
