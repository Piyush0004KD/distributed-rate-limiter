package com.ratelimiter.distributed_rate_limiter.ratelimiter;


public interface RateLimiter {
    Decision tryAcquire(String key , int limit , int windowSeconds);
}
