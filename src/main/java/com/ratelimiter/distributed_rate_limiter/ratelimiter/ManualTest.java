package com.ratelimiter.distributed_rate_limiter.ratelimiter;

import com.ratelimiter.distributed_rate_limiter.ratelimiter.core.Decision;
import com.ratelimiter.distributed_rate_limiter.ratelimiter.core.RateLimitPolicy;
import com.ratelimiter.distributed_rate_limiter.ratelimiter.core.RateLimitRequest;
import com.ratelimiter.distributed_rate_limiter.ratelimiter.core.RateLimiter;
import com.ratelimiter.distributed_rate_limiter.ratelimiter.local.LocalRateLimiter;
import com.ratelimiter.distributed_rate_limiter.ratelimiter.redis.RedisRateLimiter;
import redis.clients.jedis.JedisPool;

public class ManualTest {

    public static void main(String[] args) {

        String mode = args.length > 0 ? args[0] : "local";

        RateLimiter limiter = mode.equals("redis")
                ? new RedisRateLimiter(new JedisPool("localhost", 6380))
                : new LocalRateLimiter();

        RateLimitPolicy policy = new RateLimitPolicy(5, 60);

        RateLimitRequest request =
                new RateLimitRequest("user:1", policy);

        for (int i = 1; i <= 8; i++) {

            Decision d = limiter.tryAcquire(request);

            System.out.println(
                    "Request " + i + " -> "
                            + (d.allowed() ? "ALLOWED" : "BLOCKED")
                            + " (remaining: " + d.remaining() + ")"
            );
        }
    }
}