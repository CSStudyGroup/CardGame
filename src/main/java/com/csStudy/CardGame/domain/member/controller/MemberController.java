package com.csStudy.CardGame.domain.member.controller;

import com.csStudy.CardGame.domain.member.dto.LoginRequestForm;
import com.csStudy.CardGame.domain.member.dto.RegisterRequestForm;
import com.csStudy.CardGame.domain.member.dto.MemberDetails;
import com.csStudy.CardGame.domain.refreshtoken.dto.RefreshTokenDto;
import com.csStudy.CardGame.security.HashcodeProvider;
import com.csStudy.CardGame.security.JwtTokenProvider;
import com.csStudy.CardGame.security.SecurityUtil;
import com.csStudy.CardGame.domain.member.service.MemberService;
import com.csStudy.CardGame.domain.refreshtoken.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
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

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequestForm form, HttpServletRequest request) {
        SecurityContext securityContext = SecurityContextHolder.getContext();

        try {
            // userName, password 로 인증
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            form.getUserEmail(),
                            form.getPassword()
                    );
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            securityContext.setAuthentication(authentication);

            // access token, refresh token 발급
            Map<String, String> tokens = jwtTokenProvider.generateTokens((MemberDetails) authentication.getPrincipal());

            // access token 쿠키
            ResponseCookie accessTokenCookie = ResponseCookie.from("X-AUTH-TOKEN", tokens.get("accessToken"))
                    .maxAge(3600)
                    .path("/")
                    .build();

            // refresh token 쿠키
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

            // refresh token 쿠키
            ResponseCookie refreshTokenCookie = ResponseCookie.from("X-REFRESH-TOKEN", refreshTokenDto.getId())
                    .httpOnly(true)
                    .path("/")
                    .maxAge(3600 * 24 * 7)
                    .build();

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString(), refreshTokenCookie.toString())
                    .body(Map.of("ACCESS_TOKEN", tokens.get("accessToken")));
        }
        catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        for (Cookie cookie: request.getCookies()) {
            if (cookie.getName().equals("X-REFRESH-TOKEN")) {
                refreshTokenService.deleteById(cookie.getValue());
            }
        }
        ResponseCookie cookie = ResponseCookie.from("X-REFRESH-TOKEN", "")
                .maxAge(0)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
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
