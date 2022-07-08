package com.csStudy.CardGame.domain.refreshtoken.mapper;

import com.csStudy.CardGame.domain.refreshtoken.entity.RefreshToken;
import com.csStudy.CardGame.domain.refreshtoken.dto.RefreshTokenDto;

public interface RefreshTokenMapper {
    RefreshToken toEntity(RefreshTokenDto refreshTokenDto);
    RefreshTokenDto toDto(RefreshToken refreshToken);
}
