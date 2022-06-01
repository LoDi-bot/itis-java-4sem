package ru.itis.stogram.mappers;


import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.itis.stogram.dto.AccountDto;
import ru.itis.stogram.dto.SignUpForm;
import ru.itis.stogram.models.Account;

@RequiredArgsConstructor
@Component
public class AccountsMapper {

    private final PasswordEncoder passwordEncoder;

    public AccountDto toAccountDto(Account account) {
        return AccountDto.builder()
                .id(account.getId())
                .nick(account.getNick())
                .avatarId(account.getAvatar() == null ? 0 : account.getAvatar().getId())
                .build();
    }

    public Account toAccount(SignUpForm signUpForm) {
        return Account.builder()
                .role(Account.Role.USER)
                .state(Account.State.NOT_CONFIRMED)
                .nick(signUpForm.getNick())
                .email(signUpForm.getEmail())
                .password(passwordEncoder.encode(signUpForm.getPassword()))
                .build();
    }
}
