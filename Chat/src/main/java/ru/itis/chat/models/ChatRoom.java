package ru.itis.chat.models;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

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

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "chats")
    @ToString.Exclude
    private Set<User> participants;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "chat")
    @ToString.Exclude
    private List<Message> messages;

    @Enumerated(value = EnumType.STRING)
    private Type type;

    @Enumerated(value = EnumType.STRING)
    private State state;

}
