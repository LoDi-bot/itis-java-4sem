package ru.itis.stogram.controllers;

import com.auth0.jwt.JWT;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.itis.stogram.dto.ProfileDto;
import ru.itis.stogram.dto.SubscriptionDto;
import ru.itis.stogram.models.Subscription;
import ru.itis.stogram.services.SubscriptionService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/subscriptions")
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @PostMapping("/subscribe")
    public ResponseEntity<SubscriptionDto> subscribeToAuthor(@RequestParam("authorId") Long authorId,
                                                             Authentication authentication) {
        Long subscriberId = Long.valueOf(JWT.decode(authentication.getPrincipal().toString()).getSubject());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(subscriptionService.subscribeToAuthor(authorId, subscriberId));
    }

    @DeleteMapping("/unsubscribe")
    public void unsubscribeFromAuthor(@RequestParam("authorId") Long authorId,
                               Authentication authentication) {
        Long subscriberId = Long.valueOf(JWT.decode(authentication.getPrincipal().toString()).getSubject());

        subscriptionService.unsubscribeFromAuthor(authorId, subscriberId);
    }

    @GetMapping("/subscribers")
    public ResponseEntity<List<ProfileDto>> getSubscribers(Authentication authentication) {
        Long accountId = Long.valueOf(JWT.decode(authentication.getPrincipal().toString()).getSubject());

        return ResponseEntity.ok(subscriptionService.getSubscribers(accountId));
    }
}
