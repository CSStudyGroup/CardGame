package com.csStudy.CardGame.mapper;

import com.csStudy.CardGame.domain.RefreshToken;
import com.csStudy.CardGame.dto.RefreshTokenDto;

public interface RefreshTokenMapper {
    RefreshToken toEntity(RefreshTokenDto refreshTokenDto);
    RefreshTokenDto toDto(RefreshToken refreshToken);
}
