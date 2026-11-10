package com.ratelimiter.distributed_rate_limiter.ratelimiter.exceptionHandler;

import com.ratelimiter.distributed_rate_limiter.ratelimiter.core.Decision;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class RateLimitExceptionHandler {

    @ExceptionHandler(RateLimitExceededException.class)
    public ResponseEntity<Map<String, Object>> handleRateLimitException(
            RateLimitExceededException ex
    ) {
        Decision decision = ex.getDecision();

        return ResponseEntity
                .status(HttpStatus.TOO_MANY_REQUESTS)
                .header("Retry-After", String.valueOf(decision.retryAfter()))
                .body(Map.of(
                        "message", "Too many requests",
                        "limit", decision.limit(),
                        "remaining", decision.remaining(),
                        "retryAfter", decision.retryAfter()
                ));
    }
}