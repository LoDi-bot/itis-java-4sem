package ru.itis.stogram.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itis.stogram.dto.ProfileDto;
import ru.itis.stogram.exceptions.AccountNotFoundException;
import ru.itis.stogram.models.Account;
import ru.itis.stogram.repositories.AccountsRepository;
import ru.itis.stogram.repositories.PostsRepository;
import ru.itis.stogram.services.ProfileService;

import java.util.function.Supplier;

@RequiredArgsConstructor
@Service
public class ProfileServiceImpl implements ProfileService {

    private final AccountsRepository accountsRepository;
    private final PostsRepository postsRepository;

    @Override
    public ProfileDto getProfileById(Long accountId) {
        Account account = accountsRepository
                .findById(accountId)
                .orElseThrow((Supplier<RuntimeException>)
                        () -> new AccountNotFoundException(String.format("Account with id %s not found", accountId)));

        return ProfileDto.builder()
                .id(account.getId())
                .nick(account.getNick())
                .avatarId(account.getAvatar() == null ? 0 : account.getAvatar().getId())
                .postsLikesSumCount(postsRepository
                        .findAllByAuthorId(accountId)
                        .stream()
                        .map(post -> post.getWhoLiked().size())
                        .reduce(0, Integer::sum))
                .build();
    }
}
