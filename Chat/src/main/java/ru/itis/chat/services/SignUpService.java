package ru.itis.chat.services;

import ru.itis.chat.dto.SignUpForm;
import ru.itis.chat.models.User;

public interface SignUpService {
    void signUp(SignUpForm signUpForm);
}
