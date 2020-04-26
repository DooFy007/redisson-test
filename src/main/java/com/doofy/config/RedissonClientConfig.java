package com.doofy.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * @ClassName
 * @Description:读取redis配置文件
 * @Author DooFy
 * @Date 2020/4/21
 * @Version
 **/
@Configuration
public class RedissonClientConfig {


    @Bean
    public RedissonClient redisson() throws IOException {
        Config config = Config.fromYAML(RedissonClientConfig.class.getClassLoader().getResource("redis-clusterServer-config.yml"));
        System.out.println(config.toJSON());
        return Redisson.create(config);
    }



}
