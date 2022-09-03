package com.csStudy.CardGame.security;

import com.csStudy.CardGame.domain.member.dto.MemberDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {
    private static final KeyPair keypair = Keys.keyPairFor(SignatureAlgorithm.RS256);
    private static final long ACCESS_TOKEN_VALID_TIME = 30 * 60 * 1000L;
    private static final long REFRESH_TOKEN_VALID_TIME = 7 * 24 * 60 * 60 * 1000L;

    public Map<String, String> generateTokens(MemberDetails memberDetails) {
        Map<String, String> tokens = new HashMap<>();
        Claims claims = Jwts.claims().setSubject(memberDetails.getEmail());
        claims.put("id", memberDetails.getId());
        claims.put("email", memberDetails.getEmail());
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
                .signWith(keypair.getPrivate())
                .compact();

        // Access Token 생성
        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_VALID_TIME))
                .signWith(keypair.getPrivate())
                .compact();

        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);
        tokens.put("publicKey", Base64.getEncoder().encodeToString(keypair.getPublic().getEncoded()));

        return tokens;
    }

    public Authentication getAuthentication(String jwtToken) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(keypair.getPrivate())
                .build()
                .parseClaimsJws(jwtToken)
                .getBody();

        Set<GrantedAuthority> authorities = (Set<GrantedAuthority>) claims.get("roles", List.class).stream()
                .map(role -> new SimpleGrantedAuthority((String) role))
                .collect(Collectors.toSet());

        MemberDetails memberDetails = MemberDetails.builder()
                .id(UUID.fromString(claims.get("id", String.class)))
                .nickname(claims.get("nickname", String.class))
                .email(claims.get("email", String.class))
                .authorities(authorities)
                .build();

        return new UsernamePasswordAuthenticationToken(
                memberDetails,
                null,
                memberDetails.getAuthorities()
        );
    }

}
