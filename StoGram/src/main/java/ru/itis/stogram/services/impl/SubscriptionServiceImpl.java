package ru.itis.stogram.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itis.stogram.dto.ProfileDto;
import ru.itis.stogram.dto.SubscriptionDto;
import ru.itis.stogram.exceptions.AccountNotFoundException;
import ru.itis.stogram.exceptions.SubscribeToMyselfException;
import ru.itis.stogram.models.Account;
import ru.itis.stogram.models.Subscription;
import ru.itis.stogram.repositories.AccountsRepository;
import ru.itis.stogram.repositories.SubscriptionsRepository;
import ru.itis.stogram.services.SubscriptionService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SubscriptionServiceImpl implements SubscriptionService {
    private final SubscriptionsRepository subscriptionsRepository;
    private final AccountsRepository accountsRepository;

    @Override
    public List<Account> getSubscriptions(Long subscriberId) {
        List<Subscription> subscriptions = subscriptionsRepository.findAllBySubscriber_Id(subscriberId);

        return subscriptions
                .stream()
                .map(subscription -> subscription.getAccount())
                .collect(Collectors.toList());
    }

    @Override
    public List<ProfileDto> getSubscribers(Long authorId) {
        List<Subscription> subscriptions = subscriptionsRepository.findAllByAccount_Id(authorId);

        return subscriptions
                .stream()
                .map(subscription -> ProfileDto.builder()
                        .id(subscription.getSubscriber().getId())
                        .nick(subscription.getSubscriber().getNick())
                        .avatarId(subscription.getSubscriber().getAvatar() == null ? 0 : subscription.getSubscriber().getAvatar().getId())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public SubscriptionDto subscribeToAuthor(Long authorId, Long subscriberId) {
        if (Objects.equals(authorId, subscriberId)) {
            throw new SubscribeToMyselfException("Can't subscribe to yourself, MAN WTF is wrong with you");
        }
        Account author = accountsRepository
                .findById(authorId)
                .orElseThrow((Supplier<RuntimeException>)
                        () -> new AccountNotFoundException(String.format("Account with id %s not found", authorId)));

        Account subscriber = accountsRepository
                .getById(subscriberId);

        Optional<Subscription> optionalSubscription = subscriptionsRepository.findByAccountAndSubscriber(author, subscriber);

        if (optionalSubscription.isPresent()) {
            return SubscriptionDto.from(optionalSubscription.get());
        } else {
            Subscription subscription = Subscription.builder()
                    .account(author)
                    .subscriber(subscriber)
                    .build();

            return SubscriptionDto.from(subscriptionsRepository.save(subscription));
        }
    }

    @Override
    public void unsubscribeFromAuthor(Long authorId, Long subscriberId) {
        Account author = accountsRepository
                .findById(authorId)
                .orElseThrow((Supplier<RuntimeException>)
                        () -> new AccountNotFoundException(String.format("Account with id %s not found", authorId)));

        Account subscriber = accountsRepository
                .getById(subscriberId);

        Optional<Subscription> optionalSubscription = subscriptionsRepository.findByAccountAndSubscriber(author, subscriber);

        optionalSubscription.ifPresent(subscriptionsRepository::delete);
    }
}
