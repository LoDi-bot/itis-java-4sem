package ru.itis.stogram.services;

import ru.itis.stogram.dto.ProfileDto;

public interface ProfileService {
    ProfileDto getProfileById(Long accountId);
}
