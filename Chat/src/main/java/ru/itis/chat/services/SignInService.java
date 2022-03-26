package ru.itis.chat.services;

import ru.itis.chat.dto.SignInForm;
import ru.itis.chat.dto.UserDto;

public interface SignInService {
    UserDto signIn(SignInForm signInForm);
}
