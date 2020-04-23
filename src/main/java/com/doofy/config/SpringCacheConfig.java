package com.doofy.config;

import org.redisson.api.RedissonClient;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @ClassName
 * @Description:
 * @Author DooFy
 * @Date 2020/4/22
 * @Version
 **/
@Configuration
public class SpringCacheConfig {
//    @Bean
//    public CacheManager cacheManager(RedissonClient redissonClient) {
//        Map<String, CacheConfig> config = new HashMap<String, CacheConfig>();
//        // 创建一个名称为"testMap"的缓存，过期时间ttl为24分钟，同时最长空闲时maxIdleTime为12分钟。
//        config.put("testMap", new CacheConfig(24 * 60 * 1000, 12 * 60 * 1000));
//        return new RedissonSpringCacheManager(redissonClient, config);
//    }

    @Bean
    public CacheManager cacheManager(RedissonClient redissonClient) throws IOException {
        return new RedissonSpringCacheManager(redissonClient, "classpath:/cache-config.yml");
    }
}
