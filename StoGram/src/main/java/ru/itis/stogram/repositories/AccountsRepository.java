package ru.itis.stogram.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.stogram.models.Account;

import java.util.Optional;

public interface AccountsRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByEmail(String email);
}
