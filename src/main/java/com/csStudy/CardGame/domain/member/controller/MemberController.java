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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

@RestController
public class MemberController {
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public MemberController(MemberService memberService,
                            JwtTokenProvider jwtTokenProvider) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/authentication")
    public ResponseEntity<TokenResponse> authentication(@RequestBody LoginRequestForm form, HttpServletRequest request) {

        return ResponseEntity.ok()
                .body(jwtTokenProvider.getTokensByUsernamePassword(
                        form.getUserEmail(),
                        form.getPassword(),
                        request
                ));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(RefreshRequest refreshRequest, HttpServletRequest request) {
        String refreshTokenKey = refreshRequest.getRefreshTokenKey();
        return ResponseEntity.ok()
                .body(jwtTokenProvider.refreshTokens(refreshTokenKey, request));
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
