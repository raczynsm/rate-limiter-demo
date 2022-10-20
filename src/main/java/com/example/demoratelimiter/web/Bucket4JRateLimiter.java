package com.example.demoratelimiter.web;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.distributed.ExpirationAfterWriteStrategy;
import io.github.bucket4j.redis.spring.cas.SpringDataRedisBasedProxyManager;
import org.springframework.data.redis.connection.RedisCommands;

import java.time.Duration;

public class Bucket4JRateLimiter implements RateLimiter {

    private final RedisCommands redisCommands;
    private final Integer limit;

    private final Duration ttl;

    public Bucket4JRateLimiter(
            RedisCommands redisCommands,
            RateLimitingSettings.EndpointConfiguration endpointConfiguration
    ) {
        this.redisCommands = redisCommands;
        this.limit = endpointConfiguration.getLimit();
        this.ttl = endpointConfiguration.getTtl();
    }

    @Override
    public boolean canProcessRequest(String key) {
        SpringDataRedisBasedProxyManager proxyManager = SpringDataRedisBasedProxyManager.builderFor(redisCommands)
                .withExpirationStrategy(ExpirationAfterWriteStrategy.basedOnTimeForRefillingBucketUpToMax(Duration.ofSeconds(10)))
                .build();

        BucketConfiguration configuration = BucketConfiguration.builder()
                .addLimit(Bandwidth.simple(limit, ttl))
                .build();
        Bucket bucket = proxyManager.builder().build(key.getBytes(), configuration);
        return bucket.tryConsume(1);
    }
}
