package ru.itis.stogram.services.impl;

import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.itis.stogram.dto.SignUpForm;
import ru.itis.stogram.exceptions.EmailAlreadyTakenException;
import ru.itis.stogram.mappers.AccountsMapper;
import ru.itis.stogram.models.Account;
import ru.itis.stogram.repositories.AccountsRepository;
import ru.itis.stogram.services.ConfirmationService;
import ru.itis.stogram.services.SignUpService;
import ru.itis.stogram.utils.EmailUtil;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Validated
@RequiredArgsConstructor
@Service
public class SignUpServiceImpl implements SignUpService {

    private final AccountsRepository accountsRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountsMapper accountsMapper;
    private final EmailUtil emailUtil;
    private final ConfirmationService confirmationService;

    @Transactional
    @Override
    public void signUp(@Valid SignUpForm signUpForm) {
        if (emailExist(signUpForm.getEmail())) {
            throw new EmailAlreadyTakenException("There is already an account with given email address");
        }

        Account account = accountsMapper.toAccount(signUpForm);
        account = accountsRepository.save(account);

        String confirmationToken = confirmationService.createConfirmationToken(account).getToken();

        Map<String, String> templateData = new HashMap<>();
        templateData.put("user_name", account.getNick());
        templateData.put("confirmation_link", "stogramm.herokuapp.com/confirmation/" + confirmationToken);

        try {
            emailUtil.sendMail(account.getEmail(),
                    "Account Confirmation",
                    "confirmation_mail.ftlh",
                    templateData);
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
    }

    private boolean emailExist(String email) {
        return accountsRepository.findByEmail(email).isPresent();
    }
}
