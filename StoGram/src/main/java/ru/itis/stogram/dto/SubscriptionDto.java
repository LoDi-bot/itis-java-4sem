package ru.itis.stogram.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.stogram.models.Post;
import ru.itis.stogram.models.Subscription;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubscriptionDto {
    private Long id;
    private Instant createdAt;
    private Instant updatedAt;
    private Long authorId;
    private Long subscriberId;

    public static SubscriptionDto from(Subscription subscription) {
        return SubscriptionDto.builder()
                .id(subscription.getId())
                .createdAt(subscription.getCreatedAt())
                .updatedAt(subscription.getUpdatedAt())
                .authorId(subscription.getAccount().getId())
                .subscriberId(subscription.getSubscriber().getId())
                .build();
    }

    public static List<SubscriptionDto> from(List<Subscription> subscriptions) {
        return subscriptions.stream().map(SubscriptionDto::from).collect(Collectors.toList());
    }
}
