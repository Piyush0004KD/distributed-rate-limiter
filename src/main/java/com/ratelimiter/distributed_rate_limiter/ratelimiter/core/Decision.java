package com.ratelimiter.distributed_rate_limiter.ratelimiter.core;

public record Decision(
        boolean allowed,
        long limit,
        long remaining,
        long resetTime,
        long retryAfter
) {

    public static Decision allowed(
            long limit,
            long remaining,
            long resetTime
    ) {
        return new Decision(
                true, limit, remaining, resetTime, 0
        );
    }

    public static Decision rejected(
            long limit,
            long resetTime,
            long retryAfter
    ) {
        return new Decision(
                false, limit, 0, resetTime, retryAfter
        );
    }
}
