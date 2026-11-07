package com.ratelimiter.distributed_rate_limiter.ratelimiter.core;

public record RateLimitPolicy(
        int limit,
        int windowSeconds
) {

    public RateLimitPolicy {
        if (limit <= 0) {
            throw new IllegalArgumentException(
                    "Limit must be greater than zero"
            );
        }

        if (windowSeconds <= 0) {
            throw new IllegalArgumentException(
                    "Window must be greater than zero"
            );
        }
    }
}