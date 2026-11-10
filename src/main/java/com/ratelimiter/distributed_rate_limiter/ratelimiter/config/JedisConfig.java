package com.ratelimiter.distributed_rate_limiter.ratelimiter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;

@Configuration
public class JedisConfig {


    @Bean(destroyMethod = "close")
    public JedisPool jedisPool(){
        return new JedisPool("localhost",6379);
    }
}
