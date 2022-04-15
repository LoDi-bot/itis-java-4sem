package ru.itis.chat.services;

import ru.itis.chat.dto.SignInForm;
import ru.itis.chat.dto.AccountDto;

public interface SignInService {
    AccountDto signIn(SignInForm signInForm);
}
