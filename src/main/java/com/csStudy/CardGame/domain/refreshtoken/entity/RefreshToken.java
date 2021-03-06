package com.csStudy.CardGame.domain.refreshtoken.entity;

import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(value = "token_repo", timeToLive = 60 * 60 * 24 * 10)
public class RefreshToken {

    @Id
    private String id;
    private String token;
    private String userEmail;
    private String userIp;
    private String userAgent;

}
