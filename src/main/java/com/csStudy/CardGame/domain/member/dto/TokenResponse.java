package com.csStudy.CardGame.domain.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TokenResponse {
    private String accessToken;
    private String refreshTokenKey;
    private String signaturePublicKey;
}
