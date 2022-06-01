package ru.itis.stogram.services;

import ru.itis.stogram.models.Account;
import ru.itis.stogram.models.ConfirmationToken;

public interface ConfirmationService {
    boolean confirm(String confirmationCode);
    ConfirmationToken createConfirmationToken(Account account);
}
