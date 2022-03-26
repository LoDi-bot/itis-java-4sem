package ru.itis.chat.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.itis.chat.dto.SignUpForm;
import ru.itis.chat.models.User;
import ru.itis.chat.repositories.UsersRepository;
import ru.itis.chat.services.SignUpService;

@RequiredArgsConstructor
@Service
public class SignUpServiceImpl implements SignUpService {
    private final UsersRepository usersRepository;

    @Override
    public void signUp(SignUpForm signUpForm) {
        User user = User.builder()
                .firstName(signUpForm.getFirstName())
                .lastName(signUpForm.getLastName())
                .email(signUpForm.getEmail())
                .hashPassword(signUpForm.getPassword())
                .build();

        usersRepository.save(user);
    }

}
