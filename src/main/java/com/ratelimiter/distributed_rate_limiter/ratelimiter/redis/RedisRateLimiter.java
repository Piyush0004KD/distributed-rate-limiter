package com.ratelimiter.distributed_rate_limiter.ratelimiter.redis;

import com.ratelimiter.distributed_rate_limiter.ratelimiter.core.Decision;
import com.ratelimiter.distributed_rate_limiter.ratelimiter.core.RateLimitRequest;
import com.ratelimiter.distributed_rate_limiter.ratelimiter.core.RateLimiter;
import lombok.AllArgsConstructor;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


@AllArgsConstructor
public class RedisRateLimiter implements RateLimiter {

    private final JedisPool jedisPool;
    private static final String SCRIPT = """
    local c = redis.call('INCR', KEYS[1])
    if c == 1 then
        redis.call('EXPIRE', KEYS[1], ARGV[1])
    end
    return c
    """;
    @Override
    public Decision tryAcquire(RateLimitRequest request) {

        String key = request.key();
        long windowSeconds = request.policy().windowSeconds();
        long limit = request.policy().limit();

        try (Jedis jedis = jedisPool.getResource()) {

            Object result = jedis.eval(
                    SCRIPT, 1, key, String.valueOf(windowSeconds)
            );

            long count = (long) result;

            long ttl = jedis.ttl(key);
            long now = System.currentTimeMillis() / 1000L;

            long resetTime = now + Math.max(0, ttl);
            long remaining = Math.max(0, limit - count);

            if (count <= limit) {
                return Decision.allowed(limit, remaining, resetTime);
            }

            return Decision.rejected(limit, resetTime, Math.max(0, ttl));
        }
    }

    }




