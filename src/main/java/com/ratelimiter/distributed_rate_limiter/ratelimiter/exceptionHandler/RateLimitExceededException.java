
package com.ratelimiter.distributed_rate_limiter.ratelimiter.exceptionHandler;

import com.ratelimiter.distributed_rate_limiter.ratelimiter.core.Decision;

public class RateLimitExceededException extends RuntimeException {

    private final Decision decision;

    public RateLimitExceededException(Decision decision) {
        super("Rate limit exceeded");
        this.decision = decision;
    }

    public Decision getDecision() {
        return decision;
    }
}
