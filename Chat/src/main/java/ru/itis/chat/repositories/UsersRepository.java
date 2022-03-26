package ru.itis.chat.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.chat.models.User;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
