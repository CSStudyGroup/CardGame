package com.csStudy.CardGame.domain.refreshtoken.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenDto {
    private String id;
    private String token;
    private String userEmail;
    private String userIp;
    private String userAgent;
}