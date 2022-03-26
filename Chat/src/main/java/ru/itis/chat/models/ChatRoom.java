package ru.itis.chat.models;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "chats")
public class ChatRoom extends AbstractEntity {

    public enum Type {
        GROUP, PERSONAL
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<User> participants;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "chatRoom")
    @ToString.Exclude
    private List<Message> messages;

    @Enumerated(value = EnumType.STRING)
    private Type type;

}
