package com.csStudy.CardGame.security;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public JwtAuthenticationFilter(
            JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    private static final List<String> IGNORE =
            List.of("/static/**",
                    "/login");

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 요청 header 로부터 토큰 resolve
        String accessToken = Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION)).orElse("");

        // 액세스 토큰이 존재할 경우
        if (accessToken.startsWith("Bearer ")) {
            jwtTokenProvider.authenticate(accessToken.split(" ")[1]);
        }

        filterChain.doFilter(request,response);
    }

    protected boolean shouldNotFilter(HttpServletRequest request) {
        return IGNORE.stream().anyMatch(ignore -> ignore.equalsIgnoreCase(request.getServletPath()));
    }
}
