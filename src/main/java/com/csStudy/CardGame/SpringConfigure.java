package com.csStudy.CardGame;

import com.csStudy.CardGame.domain.RefreshToken;
import com.csStudy.CardGame.dto.RefreshTokenDto;
import com.csStudy.CardGame.mapper.*;
import com.csStudy.CardGame.security.SecurityUtil;
import com.csStudy.CardGame.security.SecurityUtilImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;

@Configuration
public class SpringConfigure {
    @Bean
    public CardMapper cardMapper() {
        return new CardMapperImpl();
    }

    @Bean
    public CategoryMapper categoryMapper(){
        return new CategoryMapperImpl();
    }

    @Bean
    public RefreshTokenMapper refreshTokenMapper() {
        return new RefreshTokenMapperImpl();
    }

    @Bean
    public MemberMapper memberMapper() {
        return new MemberMapperImpl();
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor
        exceptionTranslationPostProcessor() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    @Bean
    public SecurityUtil securityUtil() {
        return new SecurityUtilImpl();
    }

}
