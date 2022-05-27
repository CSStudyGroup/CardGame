package com.csStudy.CardGame.service;

import com.csStudy.CardGame.dto.RefreshTokenDto;

import java.util.Optional;

public interface RefreshTokenService {
    void save(RefreshTokenDto refreshTokenDto);
    void deleteById(String id);
    Optional<RefreshTokenDto> findById(String id);
}
