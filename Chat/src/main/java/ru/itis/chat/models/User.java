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
@Table(name = "users")
public class User extends AbstractEntity {

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "hash_password")
    private String hashPassword;

    private String email;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "participants")
    @ToString.Exclude
    private List<ChatRoom> chatRooms;

    @OneToMany(mappedBy = "author")
    @ToString.Exclude
    private List<Message> messages;

}
