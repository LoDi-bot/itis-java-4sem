package ru.itis.stogram.services;

import ru.itis.stogram.dto.ProfileDto;
import ru.itis.stogram.dto.SubscriptionDto;
import ru.itis.stogram.models.Account;
import ru.itis.stogram.models.Subscription;

import java.util.List;

public interface SubscriptionService {
    List<Account> getSubscriptions(Long subscriberId);
    List<ProfileDto> getSubscribers(Long authorId);
    SubscriptionDto subscribeToAuthor(Long authorId, Long subscriberId);
    void unsubscribeFromAuthor(Long authorId, Long subscriberId);
}
