package com.csStudy.CardGame.controller;

import com.csStudy.CardGame.dto.LoginRequestForm;
import com.csStudy.CardGame.dto.MemberDto;
import com.csStudy.CardGame.dto.RefreshTokenDto;
import com.csStudy.CardGame.dto.RegisterRequestForm;
import com.csStudy.CardGame.security.HashcodeProvider;
import com.csStudy.CardGame.security.JwtTokenProvider;
import com.csStudy.CardGame.security.SecurityUtil;
import com.csStudy.CardGame.service.MemberService;
import com.csStudy.CardGame.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

@Controller
public class MemberController {
    private final UserDetailsService userDetailsService;
    private final MemberService memberService;
    private final RefreshTokenService refreshTokenService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final HashcodeProvider hashcodeProvider;
    private final SecurityUtil securityUtil;

    @Autowired
    public MemberController(UserDetailsService userDetailsService,
                            MemberService memberService,
                            RefreshTokenService refreshTokenService,
                            JwtTokenProvider jwtTokenProvider,
                            AuthenticationManager authenticationManager,
                            HashcodeProvider hashcodeProvider,
                            SecurityUtil securityUtil) {
        this.userDetailsService = userDetailsService;
        this.memberService = memberService;
        this.refreshTokenService = refreshTokenService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
        this.hashcodeProvider = hashcodeProvider;
        this.securityUtil = securityUtil;
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(LoginRequestForm form, HttpServletRequest request, HttpServletResponse response) {
        SecurityContext securityContext = SecurityContextHolder.getContext();

        // 이미 인증이 완료된 상태라면 메인 페이지로 리다이렉트
        if (securityContext.getAuthentication() != null
                && !securityContext.getAuthentication().getName().equals("anonymousUser")) {
            return "redirect:/";
        }

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
        Map<String, String> tokens = jwtTokenProvider.generateTokens(authentication);

        // access token 쿠키
        Cookie accessTokenCookie = new Cookie(
                "X-AUTH-TOKEN"
                , tokens.get("accessToken"));
        accessTokenCookie.setMaxAge(3600);
        accessTokenCookie.setPath("/");

        // refresh token 쿠키
        // refresh token의 id는 유저 이메일과 (유저 이메일 + 현재시간)을 해싱한 값을 이어붙여 생성
        RefreshTokenDto refreshTokenDto =
                RefreshTokenDto.builder()
                        .id(hashcodeProvider.generateHashcode(authentication.getName(), authentication.getName() + new Date().toString()))
                        .token(tokens.get("refreshToken"))
                        .userEmail(authentication.getName())
                        .userAgent(request.getHeader("User-Agent"))
                        .userIp(securityUtil.getIp(request))
                        .build();

        // redis 저장소에 refresh token 저장
        refreshTokenService.save(refreshTokenDto);
        Cookie refreshTokenCookie = new Cookie(
                "X-REFRESH-TOKEN"
                , refreshTokenDto.getId());
        refreshTokenCookie.setMaxAge(3600 * 24 * 7);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");

        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);

        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == "anonymous") {
            return "redirect:/";
        }
        for (Cookie cookie: request.getCookies()) {
            if (cookie.getName().equals("X-REFRESH-TOKEN")) {
                refreshTokenService.deleteById(cookie.getValue());
            }
        }
        Cookie cookie = new Cookie("X-AUTH-TOKEN", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        cookie = new Cookie("X-REFRESH-TOKEN", null);
        response.addCookie(cookie);

        SecurityContextHolder.getContext().setAuthentication(null);

        return "redirect:/";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String registerProcess(RegisterRequestForm form, HttpServletRequest request, HttpServletResponse response) {
        MemberDto registeredMember = memberService.register(form).orElse(null);
        return "redirect:/";
    }
}
