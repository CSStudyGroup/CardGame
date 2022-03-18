package com.csStudy.CardGame;

import com.csStudy.CardGame.mapper.CardMapper;
import com.csStudy.CardGame.mapper.CardMapperImpl;
import com.csStudy.CardGame.mapper.CategoryMapper;
import com.csStudy.CardGame.mapper.CategoryMapperImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
}
