package com.ratelimiter.distributed_rate_limiter.ratelimiter.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimit {

    int limit();

    int window();

    String key() default "";
}