package ru.itis.stogram.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.stogram.dto.ProfileDto;
import ru.itis.stogram.services.ProfileService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profile")
public class ProfileController {
    private final ProfileService profileService;

    @GetMapping("/{account-id}")
    public ResponseEntity<ProfileDto> getProfileById(final @PathVariable("account-id") Long accountId) {
        return ResponseEntity
                .ok(profileService.getProfileById(accountId));
    }
}
