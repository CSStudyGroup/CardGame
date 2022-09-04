package com.csStudy.CardGame.domain.member.controller;

import com.csStudy.CardGame.domain.member.dto.*;
import com.csStudy.CardGame.domain.refreshtoken.dto.RefreshTokenDto;
import com.csStudy.CardGame.exception.ApiErrorEnums;
import com.csStudy.CardGame.exception.ApiErrorException;
import com.csStudy.CardGame.security.HashcodeProvider;
import com.csStudy.CardGame.security.JwtTokenProvider;
import com.csStudy.CardGame.security.SecurityUtil;
import com.csStudy.CardGame.domain.member.service.MemberService;
import com.csStudy.CardGame.domain.refreshtoken.service.RefreshTokenService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

@RestController
public class MemberController {
    private final MemberService memberService;
    private final RefreshTokenService refreshTokenService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final HashcodeProvider hashcodeProvider;
    private final SecurityUtil securityUtil;

    @Autowired
    public MemberController(MemberService memberService,
                            RefreshTokenService refreshTokenService,
                            JwtTokenProvider jwtTokenProvider,
                            AuthenticationManager authenticationManager,
                            HashcodeProvider hashcodeProvider,
                            SecurityUtil securityUtil) {
        this.memberService = memberService;
        this.refreshTokenService = refreshTokenService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
        this.hashcodeProvider = hashcodeProvider;
        this.securityUtil = securityUtil;
    }

    @PostMapping("/authentication")
    public ResponseEntity<TokenResponse> authentication(@RequestBody LoginRequestForm form, HttpServletRequest request) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        TokenResponse tokenResponse = null;
        try {
            // userName, password 로 인증
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            form.getUserEmail(),
                            form.getPassword(),
                            null
                    );
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            securityContext.setAuthentication(authentication);

            // access token, refresh token 발급
            Map<String, String> tokens = jwtTokenProvider.generateTokens((MemberDetails) authentication.getPrincipal());

            // refresh token의 id는 유저 이메일과 (유저 이메일 + 현재시간)을 해싱한 값을 이어붙여 생성
            RefreshTokenDto refreshTokenDto =
                    RefreshTokenDto.builder()
                            .id(hashcodeProvider.generateHashcode(authentication.getName(), authentication.getName() + new Date()))
                            .token(tokens.get("refreshToken"))
                            .userEmail(authentication.getName())
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
                            null);
        }
        catch (Exception ex) {
            throw ApiErrorException
                    .createException(ApiErrorEnums.INTERNAL_SERVER_ERROR,
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            ex.getMessage());
        }

        return ResponseEntity.ok()
                .body(tokenResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(RefreshRequest refreshRequest, HttpServletRequest request) {
        String refreshTokenKey = refreshRequest.getRefreshTokenKey();

        RefreshTokenDto refreshToken = refreshTokenService.findById(refreshTokenKey).orElseThrow();

        // refresh token 에 저장된 user agent 정보와 ip 정보가 일치하는지 확인
        if (refreshToken.getUserAgent().equals(request.getHeader("User-Agent"))
                && refreshToken.getUserIp().equals(securityUtil.getIp(request))) {
            try {
                Authentication authentication = jwtTokenProvider.getAuthentication(refreshToken.getToken());

                // access token, refresh token 발급
                Map<String, String> tokens = jwtTokenProvider.generateTokens((MemberDetails) authentication.getPrincipal());

                // refresh token의 id는 유저 이메일과 (유저 이메일 + 현재시간)을 해싱한 값을 이어붙여 생성
                RefreshTokenDto refreshTokenDto =
                        RefreshTokenDto.builder()
                                .id(hashcodeProvider.generateHashcode(authentication.getName(), authentication.getName() + new Date()))
                                .token(tokens.get("refreshToken"))
                                .userEmail(authentication.getName())
                                .userAgent(request.getHeader("User-Agent"))
                                .userIp(securityUtil.getIp(request))
                                .build();

                // redis 저장소에 refresh token 저장
                refreshTokenService.save(refreshTokenDto);

                return ResponseEntity.ok()
                        .body(TokenResponse.builder()
                                .accessToken(tokens.get("accessToken"))
                                .refreshTokenKey(refreshTokenDto.getId())
                                .signaturePublicKey(tokens.get("publicKey"))
                                .build());
            }
            catch (ExpiredJwtException ex) { // refresh token 만료시
                throw ApiErrorException
                        .createException(ApiErrorEnums.EXPIRED_TOKEN,
                                HttpStatus.UNAUTHORIZED,
                                "access token expired");
            }
            catch (SignatureException ex) {
                throw ApiErrorException
                        .createException(ApiErrorEnums.INVALID_TOKEN,
                                HttpStatus.UNAUTHORIZED,
                                "invalid signature of access token");
            }
            catch (MalformedJwtException ex) {
                throw ApiErrorException
                        .createException(ApiErrorEnums.INVALID_TOKEN,
                                HttpStatus.UNAUTHORIZED,
                                "invalid format of access token");
            }
            catch (Exception ex) {
                throw ApiErrorException
                        .createException(ApiErrorEnums.INTERNAL_SERVER_ERROR,
                                HttpStatus.INTERNAL_SERVER_ERROR,
                                ex.getMessage());
            }
        }
        else {
            throw ApiErrorException
                    .createException(ApiErrorEnums.INTERNAL_SERVER_ERROR,
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            "use refresh token from different ip/user-agent");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerProcess(@RequestBody RegisterRequestForm form) {
        memberService.register(form);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/valid")
    public ResponseEntity<Boolean> checkRegisterValidation(@RequestParam String email, @RequestParam String nickname) {
        return ResponseEntity.ok()
                    .body(!memberService.checkExists(email, nickname));
    }
}
