package com.csStudy.CardGame.domain.refreshtoken.service;

import com.csStudy.CardGame.domain.refreshtoken.dto.RefreshTokenDto;

import java.util.Optional;

public interface RefreshTokenService {
    void save(RefreshTokenDto refreshTokenDto);
    void deleteById(String id);
    Optional<RefreshTokenDto> findById(String id);
}
