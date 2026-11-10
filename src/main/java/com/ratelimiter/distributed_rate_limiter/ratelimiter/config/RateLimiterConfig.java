
package com.ratelimiter.distributed_rate_limiter.ratelimiter.config;

import com.ratelimiter.distributed_rate_limiter.ratelimiter.core.RateLimiter;
import com.ratelimiter.distributed_rate_limiter.ratelimiter.local.LocalRateLimiter;
import com.ratelimiter.distributed_rate_limiter.ratelimiter.redis.RedisRateLimiter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import redis.clients.jedis.JedisPool;

@Configuration
public class RateLimiterConfig {

    @Bean
    @ConditionalOnProperty(
            name = "rate-limiter.storage",
            havingValue = "local",
            matchIfMissing = true
    )
    public RateLimiter localRateLimiter() {
        return new LocalRateLimiter();
    }

    @Bean
    @ConditionalOnProperty(
            name = "rate-limiter.storage",
            havingValue = "redis"
    )
    public RateLimiter redisRateLimiter(JedisPool jedisPool) {
        return new RedisRateLimiter(jedisPool);
    }
}
