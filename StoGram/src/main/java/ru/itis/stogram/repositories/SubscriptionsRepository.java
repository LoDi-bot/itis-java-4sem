package ru.itis.stogram.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.itis.stogram.models.Account;
import ru.itis.stogram.models.Subscription;

import java.util.List;
import java.util.Optional;

public interface SubscriptionsRepository extends JpaRepository<Subscription, Long> {
    @Query("select s from Subscription s where s.account = ?1 and s.subscriber = ?2")
    Optional<Subscription> findByAccountAndSubscriber(Account account, Account subscriber);
    @Query("select s from Subscription s where s.account.id = ?1")
    List<Subscription> findAllByAccount_Id(Long accountId);
    @Query("select s from Subscription s where s.subscriber.id = ?1")
    List<Subscription> findAllBySubscriber_Id(Long subscriberId);
}
