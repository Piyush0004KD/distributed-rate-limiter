package com.ratelimiter.distributed_rate_limiter.ratelimiter.local;


import com.ratelimiter.distributed_rate_limiter.ratelimiter.core.Decision;
import com.ratelimiter.distributed_rate_limiter.ratelimiter.core.RateLimitRequest;
import com.ratelimiter.distributed_rate_limiter.ratelimiter.core.RateLimiter;

import java.util.HashMap;
import java.util.Map;

public class LocalRateLimiter implements RateLimiter {

    private static class Window{
        long start;
        int count;
    }
    private final Map<String,Window> windows = new HashMap<>();

    @Override
    public synchronized Decision tryAcquire(RateLimitRequest request){

        String key = request.key();
        int limit = request.policy().limit();
        int windowSeconds = request.policy().windowSeconds();

        long now = System.currentTimeMillis();
        Window w = windows.get(key);

        if (w == null || now - w.start >= windowSeconds * 1000L) {
            w = new Window();
            w.start = now;
            windows.put(key, w);
        }
        w.count++;

        long remaining = Math.max(0, limit - w.count);

        long resetTime = (w.start + windowSeconds * 1000L) / 1000L;

        if (w.count <= limit) {
            return Decision.allowed(limit, remaining, resetTime);
        }

        long retryAfter = Math.max(
                0,
                (resetTime - System.currentTimeMillis() / 1000L)
        );

        return Decision.rejected(limit, resetTime, retryAfter);
    }

}
