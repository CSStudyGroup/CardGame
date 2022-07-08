package com.csStudy.CardGame.configuration;

import com.csStudy.CardGame.domain.card.mapper.CardMapper;
import com.csStudy.CardGame.domain.card.mapper.CardMapperImpl;
import com.csStudy.CardGame.domain.cardrequest.mapper.CardRequestMapper;
import com.csStudy.CardGame.domain.cardrequest.mapper.CardRequestMapperImpl;
import com.csStudy.CardGame.domain.category.mapper.CategoryMapper;
import com.csStudy.CardGame.domain.category.mapper.CategoryMapperImpl;
import com.csStudy.CardGame.domain.member.mapper.MemberMapper;
import com.csStudy.CardGame.domain.member.mapper.MemberMapperImpl;
import com.csStudy.CardGame.domain.refreshtoken.mapper.RefreshTokenMapper;
import com.csStudy.CardGame.domain.refreshtoken.mapper.RefreshTokenMapperImpl;
import com.csStudy.CardGame.security.SecurityUtil;
import com.csStudy.CardGame.security.SecurityUtilImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;

@Configuration
public class SpringConfiguration {

    @Bean
    public PersistenceExceptionTranslationPostProcessor
        exceptionTranslationPostProcessor() {
        return new PersistenceExceptionTranslationPostProcessor();
    }


}
