package com.csStudy.CardGame.configuration;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.cache.jcache.JCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.configuration.MutableConfiguration;
import java.util.Objects;

@EnableCaching
@Configuration
public class CacheConfiguration {
    @Bean
    public JCacheManagerFactoryBean jCacheManagerFactoryBean() {
        return new JCacheManagerFactoryBean();
    }

    @Bean
    public JCacheCacheManager jCacheCacheManager() {
        MutableConfiguration<Object, Object> config = new MutableConfiguration<>();
        config.setStoreByValue(false);
        Objects.requireNonNull(jCacheManagerFactoryBean().getObject()).createCache("categoryList", config);
        return new JCacheCacheManager(Objects.requireNonNull(jCacheManagerFactoryBean().getObject()));
    }

}
