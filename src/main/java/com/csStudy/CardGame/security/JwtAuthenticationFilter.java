package com.csStudy.CardGame.security;

import com.csStudy.CardGame.domain.member.dto.MemberDetails;
import com.csStudy.CardGame.domain.refreshtoken.service.RefreshTokenService;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
        // ?????? header ????????? Expired ?????? ??????
        String expired = Optional.ofNullable(request.getHeader("Expired")).orElse("");

        // Expired ??? refresh token ??????
        if (expired.equals("true")) {
            String refreshTokenKey = null;
            for(Cookie cookie: request.getCookies()) {
                // refresh token key??? ????????? key??? ????????????
                if (cookie.getName().equals("X-REFRESH-TOKEN")) {
                    refreshTokenKey = cookie.getValue();
                    break;
                }
            }

            // refresh key??? refresh token??? ????????????
            refreshTokenService.findById(refreshTokenKey)
                    .ifPresent((refreshToken) -> {
                        // refresh token??? ????????? user agent????????? ip????????? ??????????????? ??????
                        if (refreshToken.getUserAgent().equals(request.getHeader("User-Agent"))
                                && refreshToken.getUserIp().equals(securityUtil.getIp(request))) {
                            try {
                                String userEmail = jwtTokenProvider.getUserName(refreshToken.getToken());
                                MemberDetails memberDetails = (MemberDetails) userDetailsService.loadUserByUsername(userEmail);
                                UsernamePasswordAuthenticationToken authentication =
                                        new UsernamePasswordAuthenticationToken(
                                                memberDetails.getUsername(),
                                                null,
                                                memberDetails.getAuthorities()
                                        );
                                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                                SecurityContextHolder.getContext().setAuthentication(authentication);
                                Cookie cookie = new Cookie(
                                        "X-AUTH-TOKEN",
                                        jwtTokenProvider.generateAccessToken(memberDetails));
                                cookie.setPath("/");
                                cookie.setMaxAge(3600);
                                response.addCookie(cookie);
                            }
                            catch (ExpiredJwtException refreshTokenExpiredException) { // refresh token ?????????
                                response.setStatus(HttpStatus.FORBIDDEN.value());
                            }
                            catch (Exception e) {
                                response.setStatus(HttpStatus.FORBIDDEN.value());
                            }
                        }
                    });
        }
        else {
            // ?????? header ????????? ?????? resolve
            String accessToken = Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION)).orElse("");
            SecurityContext context = SecurityContextHolder.getContext();
            // ????????? ????????? ????????? ??????
            if (accessToken.startsWith("Bearer ")) {
                try {
                    String userEmail = jwtTokenProvider.getUserName(accessToken.split(" ")[1]);
                    MemberDetails memberDetails = (MemberDetails) userDetailsService.loadUserByUsername(userEmail);
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    memberDetails.getUsername(),
                                    null,
                                    memberDetails.getAuthorities()
                            );
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } catch (ExpiredJwtException accessTokenExpiredJwtException) { // ????????? ????????? ??????
                    response.setStatus(HttpStatus.FORBIDDEN.value());
                } catch (Exception exception) {   // ????????? ????????? ??????
                    response.setStatus(HttpStatus.FORBIDDEN.value());
                }
            }
        }
        filterChain.doFilter(request,response);
    }

    protected boolean shouldNotFilter(HttpServletRequest request) {
        return IGNORE.stream().anyMatch(ignore -> ignore.equalsIgnoreCase(request.getServletPath()));
    }
}
