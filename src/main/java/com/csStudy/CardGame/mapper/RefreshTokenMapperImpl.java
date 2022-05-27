package com.csStudy.CardGame.mapper;

import com.csStudy.CardGame.domain.RefreshToken;
import com.csStudy.CardGame.dto.RefreshTokenDto;

public class RefreshTokenMapperImpl implements RefreshTokenMapper {

    @Override
    public RefreshToken toEntity(RefreshTokenDto refreshTokenDto) {
        return RefreshToken.builder()
                .id(refreshTokenDto.getId())
                .token(refreshTokenDto.getToken())
                .userEmail(refreshTokenDto.getUserEmail())
                .userAgent(refreshTokenDto.getUserAgent())
                .userIp(refreshTokenDto.getUserIp())
                .build();
    }

    @Override
    public RefreshTokenDto toDto(RefreshToken refreshToken) {
        return RefreshTokenDto.builder()
                .id(refreshToken.getId())
                .token(refreshToken.getToken())
                .userEmail(refreshToken.getUserEmail())
                .userAgent(refreshToken.getUserAgent())
                .userIp(refreshToken.getUserIp())
                .build();
    }
}
