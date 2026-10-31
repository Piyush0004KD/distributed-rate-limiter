package com.ratelimiter.distributed_rate_limiter.ratelimiter;

import lombok.AllArgsConstructor;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


@AllArgsConstructor
public class RedisRateLimiter implements  RateLimiter {

    private final JedisPool jedisPool;

    @Override
    public Decision tryAcquire(String key , int limit , int windowSeconds){

        try(Jedis jedis = jedisPool.getResource()){
            long count = jedis.incr(key);

            if(count==1){
                jedis.expire(key,windowSeconds);
            }

            boolean allowed = count<=limit;
            long remaining = Math.max(0,limit-count);

            return new Decision(allowed,remaining);

        }

    }



}
