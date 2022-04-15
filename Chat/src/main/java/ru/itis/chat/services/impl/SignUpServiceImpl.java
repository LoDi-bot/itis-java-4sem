package ru.itis.chat.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itis.chat.dto.SignUpForm;
import ru.itis.chat.exceptions.ErrorEntity;
import ru.itis.chat.exceptions.ValidationException;
import ru.itis.chat.mapper.AccountsMapper;
import ru.itis.chat.models.Account;
import ru.itis.chat.repositories.AccountsRepository;
import ru.itis.chat.services.SignUpService;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class SignUpServiceImpl implements SignUpService {
    private final AccountsRepository accountsRepository;
    private final AccountsMapper accountsMapper;
    private final Validator validator;

    @Override
    public void signUp(SignUpForm signUpForm) {
        Set<ConstraintViolation<SignUpForm>> violations = validator.validate(signUpForm);
        if(!violations.isEmpty()) {
            throw new ValidationException(violations.stream().findFirst().get().getMessage());
        }

        if (accountsRepository.findByEmail(signUpForm.getEmail()).isPresent()) {
            throw new ValidationException(ErrorEntity.EMAIL_ALREADY_TAKEN);
        }

        Account account = accountsMapper.toAccount(signUpForm);
        accountsRepository.save(account);
    }

}
