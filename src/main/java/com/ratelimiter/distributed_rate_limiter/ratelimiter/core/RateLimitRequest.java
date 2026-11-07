package com.ratelimiter.distributed_rate_limiter.ratelimiter.core;

import java.util.Objects;

public record RateLimitRequest(
        String key,
        RateLimitPolicy policy
) {

    public RateLimitRequest {
        if (key == null || key.isBlank()) {
            throw new IllegalArgumentException(
                    "Key cannot be null or empty"
            );
        }

        Objects.requireNonNull(
                policy,
                "Policy cannot be null"
        );
    }
}