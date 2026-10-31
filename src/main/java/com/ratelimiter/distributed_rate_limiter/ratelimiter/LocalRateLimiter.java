package com.ratelimiter.distributed_rate_limiter.ratelimiter;


import java.util.HashMap;
import java.util.Map;

public class LocalRateLimiter implements RateLimiter{

    private static class Window{
        long start;
        int count;
    }
    private final Map<String,Window> windows = new HashMap<>();

    @Override
    public synchronized Decision tryAcquire(String key , int limit , int windowSeconds){
        long now = System.currentTimeMillis();
        Window w = windows.get(key);
        if (w == null || now - w.start >= windowSeconds * 1000L) {
            w = new Window();
            w.start = now;
            windows.put(key, w);
        }
        w.count++;
        boolean allowed = w.count <= limit;
        long remaining = Math.max(0, limit - w.count);

        return new Decision(allowed, remaining);
    }

}
