package com.ratelimiter.distributed_rate_limiter.ratelimiter.core;

public interface RateLimiter {

    Decision tryAcquire(RateLimitRequest request);

}