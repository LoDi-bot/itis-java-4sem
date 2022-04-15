package ru.itis.chat.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.itis.chat.dto.SignInForm;
import ru.itis.chat.dto.AccountDto;
import ru.itis.chat.exceptions.ErrorEntity;
import ru.itis.chat.exceptions.ValidationException;
import ru.itis.chat.mapper.AccountsMapper;
import ru.itis.chat.models.Account;
import ru.itis.chat.repositories.AccountsRepository;
import ru.itis.chat.services.SignInService;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;
import java.util.function.Supplier;

@RequiredArgsConstructor
@Service
public class SignInServiceImpl implements SignInService {
    private final AccountsRepository accountsRepository;
    private final AccountsMapper accountsMapper;
    private final PasswordEncoder passwordEncoder;
    private final Validator validator;

    @Override
    public AccountDto signIn(SignInForm signInForm) {
        Set<ConstraintViolation<SignInForm>> violations = validator.validate(signInForm);
        if(!violations.isEmpty()) {
            throw new ValidationException(violations.stream().findFirst().get().getMessage());
        }

        Account account = accountsRepository
                .findByEmail(signInForm.getEmail())
                .orElseThrow((Supplier<RuntimeException>)
                        () -> new ValidationException(ErrorEntity.ACCOUNT_NOT_FOUND));

        if (passwordEncoder.matches(signInForm.getPassword(), account.getPassword())) {
            return accountsMapper.toAccountDto(account);
        } else {
            throw new ValidationException(ErrorEntity.INCORRECT_PASSWORD);
        }
    }
}
