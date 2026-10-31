package com.ratelimiter.distributed_rate_limiter.ratelimiter;

public record Decision(boolean allowed , long remaining ) {
}
