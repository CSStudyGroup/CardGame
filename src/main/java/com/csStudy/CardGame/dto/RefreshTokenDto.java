package com.csStudy.CardGame.dto;

import lombok.*;

import java.util.Date;

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