package com.ratelimiter.distributed_rate_limiter.ratelimiter.aspect;

import com.ratelimiter.distributed_rate_limiter.ratelimiter.annotation.RateLimit;
import com.ratelimiter.distributed_rate_limiter.ratelimiter.core.Decision;
import com.ratelimiter.distributed_rate_limiter.ratelimiter.core.RateLimitPolicy;
import com.ratelimiter.distributed_rate_limiter.ratelimiter.core.RateLimitRequest;
import com.ratelimiter.distributed_rate_limiter.ratelimiter.core.RateLimiter;

import com.ratelimiter.distributed_rate_limiter.ratelimiter.exceptionHandler.RateLimitExceededException;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class RateLimitAspect {

    private final RateLimiter rateLimiter;

    @Around("@annotation(rateLimit)")
    public Object rateLimit(
           ProceedingJoinPoint proceedingJoinPoint,
           RateLimit rateLimit
    )throws Throwable{
        String userKey = rateLimit.key();
        int limit = rateLimit.limit();
        int window =rateLimit.window();

        RateLimitPolicy policy =
                new RateLimitPolicy(limit, window);

        RateLimitRequest request =
                new RateLimitRequest(userKey, policy);


        Decision decision = rateLimiter.tryAcquire(request);

        if(!decision.allowed()){
            throw new RateLimitExceededException(decision);
        }

        return proceedingJoinPoint.proceed();
    }

}
