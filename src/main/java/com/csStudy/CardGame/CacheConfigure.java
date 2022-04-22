package com.csStudy.CardGame;

import net.sf.ehcache.Cache;
import net.sf.ehcache.config.CacheConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@EnableCaching
@Configuration
public class CacheConfigure {
    @Bean
    public EhCacheManagerFactoryBean ehCacheManagerFactoryBean() {
        return new EhCacheManagerFactoryBean();
    }

    @Bean
    public EhCacheCacheManager ehCacheCacheManager() {
        Cache categoryListCache = new Cache(new CacheConfiguration()
                .eternal(false)
                .timeToLiveSeconds(0)
                .timeToIdleSeconds(0)
                .maxEntriesLocalHeap(0)
                .memoryStoreEvictionPolicy("LRU")
                .name("categoryList"));

        Objects.requireNonNull(ehCacheManagerFactoryBean().getObject()).addCache(categoryListCache);
        return new EhCacheCacheManager(Objects.requireNonNull(ehCacheManagerFactoryBean().getObject()));
    }
}
