package com.csStudy.CardGame.security;
import com.csStudy.CardGame.domain.member.dto.MemberDetails;
import com.csStudy.CardGame.domain.member.dto.TokenResponse;
import com.csStudy.CardGame.domain.refreshtoken.dto.RefreshTokenDto;
import com.csStudy.CardGame.domain.refreshtoken.service.RefreshTokenService;
import com.csStudy.CardGame.exception.ApiErrorEnums;
import com.csStudy.CardGame.exception.ApiErrorException;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.security.KeyPair;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {
    private static final KeyPair keypair = Keys.keyPairFor(SignatureAlgorithm.RS256);
    private static final long ACCESS_TOKEN_VALID_TIME = 30 * 60 * 1000L;
    private static final long REFRESH_TOKEN_VALID_TIME = 7 * 24 * 60 * 60 * 1000L;
    private final UserDetailsService userDetailsService;
    private final RefreshTokenService refreshTokenService;
    private final HashcodeProvider hashcodeProvider;
    private final SecurityUtil securityUtil;

    public JwtTokenProvider(
            UserDetailsService userDetailsService,
            RefreshTokenService refreshTokenService,
            HashcodeProvider hashcodeProvider,
            SecurityUtil securityUtil
    ) {
        this.userDetailsService = userDetailsService;
        this.refreshTokenService = refreshTokenService;
        this.hashcodeProvider = hashcodeProvider;
        this.securityUtil = securityUtil;
    }

    public TokenResponse getTokenSet(MemberDetails memberDetails, HttpServletRequest request) {
        TokenResponse tokenResponse = null;

        try {
            // access token, refresh token 발급
            Map<String, String> tokens = generateTokens(memberDetails);

            // refresh token 의 id는 유저 이메일과 (유저 이메일 + 현재시간)을 해싱한 값을 이어붙여 생성
            RefreshTokenDto refreshTokenDto =
                    RefreshTokenDto.builder()
                            .id(hashcodeProvider.generateHashcode(memberDetails.getEmail(), memberDetails.getEmail() + new Date()))
                            .token(tokens.get("refreshToken"))
                            .userEmail(memberDetails.getEmail())
                            .userAgent(request.getHeader("User-Agent"))
                            .userIp(securityUtil.getIp(request))
                            .build();

            // redis 저장소에 refresh token 저장
            refreshTokenService.save(refreshTokenDto);

            tokenResponse = TokenResponse.builder()
                    .accessToken(tokens.get("accessToken"))
                    .refreshTokenKey(refreshTokenDto.getId())
                    .signaturePublicKey(tokens.get("publicKey"))
                    .build();
        }
        catch (BadCredentialsException ex) {
            throw ApiErrorException
                    .createException(ApiErrorEnums.INVALID_EMAIL_OR_PASSWORD,
                            HttpStatus.UNAUTHORIZED,
                            null,
                            null);
        }
        catch (Exception ex) {
            throw ApiErrorException
                    .createException(ApiErrorEnums.INTERNAL_SERVER_ERROR,
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            null,
                            ex.getMessage());
        }

        return tokenResponse;
    }

    public TokenResponse refreshTokens(String refreshTokenKey, HttpServletRequest request) {
        RefreshTokenDto refreshToken = refreshTokenService.findById(refreshTokenKey).orElseThrow(
                () -> ApiErrorException.createException(
                        ApiErrorEnums.RESOURCE_NOT_FOUND,
                        HttpStatus.NOT_FOUND,
                        null,
                        null
                )
        );

        Claims claims = getClaims(refreshToken.getToken());

        // refresh token 에 저장된 user agent 정보와 ip 정보가 일치하는지 확인
        if (refreshToken.getUserAgent().equals(request.getHeader("User-Agent"))
                && refreshToken.getUserIp().equals(securityUtil.getIp(request))) {
            MemberDetails memberDetails = (MemberDetails) userDetailsService.loadUserByUsername(
                    claims.get("email", String.class)
            );

            // access token, refresh token 발급
            Map<String, String> tokens = generateTokens(memberDetails);

            // refresh token 의 id는 유저 이메일과 (유저 이메일 + 현재시간)을 해싱한 값을 이어붙여 생성
            RefreshTokenDto refreshTokenDto =
                    RefreshTokenDto.builder()
                            .id(hashcodeProvider.generateHashcode(memberDetails.getEmail(), memberDetails.getEmail() + new Date()))
                            .token(tokens.get("refreshToken"))
                            .userEmail(memberDetails.getEmail())
                            .userAgent(request.getHeader("User-Agent"))
                            .userIp(securityUtil.getIp(request))
                            .build();

            // redis 저장소에 refresh token 저장
            refreshTokenService.save(refreshTokenDto);

            return TokenResponse.builder()
                    .accessToken(tokens.get("accessToken"))
                    .refreshTokenKey(refreshTokenDto.getId())
                    .signaturePublicKey(tokens.get("publicKey"))
                    .build();
        }
        else {
            throw ApiErrorException
                    .createException(ApiErrorEnums.INTERNAL_SERVER_ERROR,
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            null,
                            "use refresh token from different ip/user-agent");
        }
    }

    public void authenticate(String jwtToken) {
        Claims claims = getClaims(jwtToken);

        MemberDetails memberDetails = (MemberDetails) userDetailsService.loadUserByUsername(claims.get("email", String.class));

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        memberDetails,
                        null,
                        memberDetails.getAuthorities()));
    }

    private Map<String, String> generateTokens(MemberDetails memberDetails) {
        Map<String, String> tokens = new HashMap<>();
        Claims claims = Jwts.claims().setSubject(memberDetails.getEmail());
        claims.put("id", memberDetails.getId());
        claims.put("email", memberDetails.getEmail());
        claims.put("nickname", memberDetails.getNickname());
        claims.put("roles", memberDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        // Refresh Token 생성
        Date now = new Date();
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

    private Claims getClaims(String jwtToken) {
        Claims claims = null;
        try {
            claims = Jwts
                    .parserBuilder()
                    .setSigningKey(keypair.getPrivate())
                    .build()
                    .parseClaimsJws(jwtToken)
                    .getBody();
        }
        catch (ExpiredJwtException ex) { // refresh token 만료시
            throw ApiErrorException
                    .createException(ApiErrorEnums.EXPIRED_TOKEN,
                            HttpStatus.UNAUTHORIZED,
                            null,
                            "access token expired");
        }
        catch (SignatureException ex) {
            throw ApiErrorException
                    .createException(ApiErrorEnums.INVALID_TOKEN,
                            HttpStatus.UNAUTHORIZED,
                            null,
                            "invalid signature of access token");
        }
        catch (MalformedJwtException ex) {
            throw ApiErrorException
                    .createException(ApiErrorEnums.INVALID_TOKEN,
                            HttpStatus.UNAUTHORIZED,
                            null,
                            "invalid format of access token");
        }
        catch (Exception ex) {
            throw ApiErrorException
                    .createException(ApiErrorEnums.INTERNAL_SERVER_ERROR,
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            null,
                            ex.getMessage());
        }
        return claims;
    }
}
