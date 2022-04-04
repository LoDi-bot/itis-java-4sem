package ru.itis.chat.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.itis.chat.dto.SignInForm;
import ru.itis.chat.dto.UserDto;
import ru.itis.chat.models.User;
import ru.itis.chat.repositories.UsersRepository;
import ru.itis.chat.services.SignInService;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SignInServiceImpl implements SignInService {

    private final UsersRepository usersRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto signIn(SignInForm signInForm) {
        Optional<User> optionalUser = usersRepository.findByEmail(signInForm.getEmail());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (passwordEncoder.matches(signInForm.getPassword(), user.getPassword())) {
                return UserDto.from(user);
            }
        }
        return null;
    }
}
