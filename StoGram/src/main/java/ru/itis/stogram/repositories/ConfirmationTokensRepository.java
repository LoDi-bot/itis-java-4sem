package ru.itis.stogram.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.stogram.models.ConfirmationToken;

import java.util.Optional;

public interface ConfirmationTokensRepository extends JpaRepository<ConfirmationToken, Long> {
    Optional<ConfirmationToken> findByToken(String token);
}