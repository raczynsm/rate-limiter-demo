package com.example.demoratelimiter.web;

import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;

public class CustomRateLimiter implements RateLimiter {

    private final RedisTemplate<String, Integer> redisTemplate;
    private final Integer limit;

    private final Duration ttl;

    public CustomRateLimiter(
            RedisTemplate<String, Integer> redisTemplate,
            RateLimitingSettings.EndpointConfiguration endpointConfiguration
    ) {
        this.redisTemplate = redisTemplate;
        this.limit = endpointConfiguration.getLimit();
        this.ttl = endpointConfiguration.getTtl();
    }

    @Override
    public boolean canProcessRequest(String key) {
        Integer count = redisTemplate.opsForValue().get(key);

        if (count == null) {
            redisTemplate.opsForValue().setIfAbsent(key, 1, ttl);
            return true;
        }
        if(count >= limit) {
            return false;
        } else {
            redisTemplate.opsForValue().increment(key);
            return true;
        }
    }
}
