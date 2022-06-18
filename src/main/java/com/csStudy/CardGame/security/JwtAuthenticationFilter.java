package com.csStudy.CardGame.security;

import com.csStudy.CardGame.dto.RefreshTokenDto;
import com.csStudy.CardGame.dto.SecuredMember;
import com.csStudy.CardGame.service.RefreshTokenService;
import com.csStudy.CardGame.service.RefreshTokenServiceImpl;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;
    private final RefreshTokenService refreshTokenService;
    private final SecurityUtil securityUtil;

    @Autowired
    public JwtAuthenticationFilter(
            JwtTokenProvider jwtTokenProvider,
            UserDetailsService userDetailsService,
            RefreshTokenService refreshTokenService,
            SecurityUtil securityUtil) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
        this.refreshTokenService = refreshTokenService;
        this.securityUtil = securityUtil;
    }

    private static final List<String> IGNORE =
            List.of("/static/**",
                    "/login");

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 요청 header 로부터 Expired 여부 확인
        String expired = Optional.ofNullable(request.getHeader("Expired")).orElse("");

        // Expired 시 refresh token 확인
        if (expired.equals("true")) {
            String refreshTokenKey = null;
            for(Cookie cookie: request.getCookies()) {
                // refresh token key가 있다면 key를 가져온다
                if (cookie.getName().equals("X-REFRESH-TOKEN")) {
                    refreshTokenKey = cookie.getValue();
                    break;
                }
            }

            // refresh key로 refresh token을 가져온다
            refreshTokenService.findById(refreshTokenKey)
                    .ifPresent((refreshToken) -> {
                        // refresh token에 저장된 user agent정보와 ip정보가 일치하는지 확인
                        if (refreshToken.getUserAgent().equals(request.getHeader("User-Agent"))
                                && refreshToken.getUserIp().equals(securityUtil.getIp(request))) {
                            try {
                                String userEmail = jwtTokenProvider.getUserName(refreshToken.getToken());
                                SecuredMember securedMember = (SecuredMember) userDetailsService.loadUserByUsername(userEmail);
                                UsernamePasswordAuthenticationToken authentication =
                                        new UsernamePasswordAuthenticationToken(
                                                securedMember.getUsername(),
                                                null,
                                                securedMember.getAuthorities()
                                        );
                                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                                SecurityContextHolder.getContext().setAuthentication(authentication);
                                Cookie cookie = new Cookie(
                                        "X-AUTH-TOKEN",
                                        jwtTokenProvider.generateAccessToken(securedMember));
                                cookie.setPath("/");
                                cookie.setMaxAge(3600);
                                response.addCookie(cookie);
                            }
                            catch (ExpiredJwtException refreshTokenExpiredException) { // refresh token 만료시
                                response.setStatus(HttpStatus.FORBIDDEN.value());
                            }
                            catch (Exception e) {
                                response.setStatus(HttpStatus.FORBIDDEN.value());
                            }
                        }
                    });
        }
        else {
            // 요청 header 로부터 토큰 resolve
            String accessToken = Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION)).orElse("");
            SecurityContext context = SecurityContextHolder.getContext();
            // 액세스 토큰이 존재할 경우
            if (accessToken.startsWith("Bearer ")) {
                try {
                    String userEmail = jwtTokenProvider.getUserName(accessToken.split(" ")[1]);
                    SecuredMember securedMember = (SecuredMember) userDetailsService.loadUserByUsername(userEmail);
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    securedMember.getUsername(),
                                    null,
                                    securedMember.getAuthorities()
                            );
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } catch (ExpiredJwtException accessTokenExpiredJwtException) { // 만료된 토큰일 경우
                    response.setStatus(HttpStatus.FORBIDDEN.value());
                } catch (Exception exception) {   // 잘못된 토큰일 경우
                    response.setStatus(HttpStatus.FORBIDDEN.value());
                }
            }
        }
        filterChain.doFilter(request,response);
    }

    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return IGNORE.stream().anyMatch(ignore -> ignore.equalsIgnoreCase(request.getServletPath()));
    }
}
