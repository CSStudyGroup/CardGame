package com.csStudy.CardGame.security;

import com.csStudy.CardGame.domain.member.dto.MemberDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {
    private static final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final long ACCESS_TOKEN_VALID_TIME = 30 * 60 * 1000L;
    private static final long REFRESH_TOKEN_VALID_TIME = 7 * 24 * 60 * 60 * 1000L;

    public Map<String, String> generateTokens(MemberDetails memberDetails) {
        Map<String, String> tokens = new HashMap<>();
        Claims claims = Jwts.claims().setSubject(memberDetails.getEmail());
        claims.put("nickname", memberDetails.getNickname());
        claims.put("roles", memberDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));
        Date now = new Date();

        // Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_VALID_TIME))
                .signWith(secretKey)
                .compact();

        // Access Token 생성
        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_VALID_TIME))
                .signWith(secretKey)
                .compact();

        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken",refreshToken);

        return tokens;
    }

    public String generateAccessToken(MemberDetails memberDetails) {
        Claims claims = Jwts.claims().setSubject(memberDetails.getEmail());
        claims.put("nickname", memberDetails.getNickname());
        claims.put("roles", memberDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));
        Date now = new Date();
        // Access Token 생성
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_VALID_TIME))
                .signWith(secretKey)
                .compact();
    }

    public String getUserName(String jwtToken) {
        return Jwts
                .parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(jwtToken)
                .getBody()
                .getSubject();
    }


}
