package com.example.demoratelimiter.web;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Data
@ConfigurationProperties(prefix = "rate-limiting")
public class RateLimitingSettings {
    Map<String, EndpointConfiguration> endpoints = new HashMap<>();

    @Data
    public static class EndpointConfiguration {
        private int limit;
        private Duration ttl;
    }
}

