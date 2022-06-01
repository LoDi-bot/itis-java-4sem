package ru.itis.stogram.services.impl;

import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itis.stogram.exceptions.ConfirmationLinkExpiredException;
import ru.itis.stogram.models.Account;
import ru.itis.stogram.models.ConfirmationToken;
import ru.itis.stogram.repositories.AccountsRepository;
import ru.itis.stogram.repositories.ConfirmationTokensRepository;
import ru.itis.stogram.services.ConfirmationService;
import ru.itis.stogram.utils.EmailUtil;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;

@RequiredArgsConstructor
@Service
public class ConfirmationServiceImpl implements ConfirmationService {

    private final AccountsRepository accountsRepository;
    private final ConfirmationTokensRepository confirmationTokensRepository;
    private final EmailUtil emailUtil;

    @Transactional
    @Override
    public boolean confirm(String confirmationCode) {
        Optional<ConfirmationToken> optionalConfirmationToken = confirmationTokensRepository.findByToken(confirmationCode);
        if (optionalConfirmationToken.isPresent()) {
            ConfirmationToken confirmationToken = optionalConfirmationToken.get();

            Account account = optionalConfirmationToken.get().getAccount();

            Calendar cal = Calendar.getInstance();
            if ((confirmationToken.getExpirationDate().getTime() - cal.getTime().getTime()) <= 0) {
                confirmationToken.setExpirationDate(calculateExpiryDate());
                confirmationToken.setToken(UUID.randomUUID().toString());
                confirmationTokensRepository.save(confirmationToken);

                Map<String, String> data = new HashMap<>();
                data.put("user_name", account.getNick());
                data.put("confirmation_link", "stogramm.herokuapp.com/confirmation/" + confirmationToken.getToken());

                try {
                    emailUtil.sendMail(account.getEmail(),
                            "Account Confirmation",
                            "confirmation_mail.ftlh",
                            data);
                } catch (IOException | TemplateException e) {
                    e.printStackTrace();
                }

                throw new ConfirmationLinkExpiredException("Current confirmation link has already been expired, please check your email for the new one");
            }

            account.setState(Account.State.CONFIRMED);
            confirmationTokensRepository.delete(confirmationToken);
            accountsRepository.save(account);

            return true;
        } else {
            return false;
        }
    }

    @Override
    public ConfirmationToken createConfirmationToken(Account account) {
        ConfirmationToken confirmationToken = ConfirmationToken.builder()
                .token(UUID.randomUUID().toString())
                .account(account)
                .expirationDate(calculateExpiryDate())
                .build();

        return confirmationTokensRepository.save(confirmationToken);
    }

    private Date calculateExpiryDate() {
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(new Date().getTime());
        cal.add(Calendar.MINUTE, 1440);
        return new Date(cal.getTime().getTime());
    }

}
