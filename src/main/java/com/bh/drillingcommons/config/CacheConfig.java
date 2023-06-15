package com.bh.drillingcommons.config;

import com.github.benmanes.caffeine.cache.CaffeineSpec;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CaffeineCacheManager appCacheManager() {
        CaffeineCacheManager manager = new CaffeineCacheManager();
        CaffeineSpec spec = CaffeineSpec
                .parse("maximumSize=1000 ,expireAfterWrite=24h");
        manager.setCaffeineSpec(spec);
        return manager;
    }
}
