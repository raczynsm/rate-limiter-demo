package com.example.demoratelimiter.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisCommands;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@EnableConfigurationProperties(RateLimitingSettings.class)
public class RateLimitingConfiguration {

    private final RateLimitingSettings settings;

    @Autowired
    public RateLimitingConfiguration(RateLimitingSettings settings) {
        this.settings = settings;
    }

    @Bean
    @ConditionalOnProperty(name = "library.enabled", havingValue = "true")
    public FilterRegistrationBean<RateLimitingFilter> pingEndpointWithBucket4J(
            RedisCommands redisCommands
    ) {
        RateLimiter rateLimiter = new Bucket4JRateLimiter(redisCommands, settings.endpoints.get("ping"));
        FilterRegistrationBean<RateLimitingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RateLimitingFilter(rateLimiter));
        registrationBean.addUrlPatterns("/ping");
        registrationBean.setName("pingRateLimiter");
        return registrationBean;
    }

    @Bean
    @ConditionalOnProperty(name = "library.enabled", havingValue = "true")
    public FilterRegistrationBean<RateLimitingFilter> pongEndpointWithBucket4J(
            RedisCommands redisCommands
    ) {
        RateLimiter rateLimiter = new Bucket4JRateLimiter(redisCommands, settings.endpoints.get("pong"));
        FilterRegistrationBean<RateLimitingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RateLimitingFilter(rateLimiter));
        registrationBean.addUrlPatterns("/pong");
        registrationBean.setName("pongRateLimiter");
        return registrationBean;
    }

    @Bean
    @ConditionalOnProperty(name = "custom.enabled", havingValue = "true")
    public FilterRegistrationBean<RateLimitingFilter> pingEndpointWithCustom(
            RedisTemplate<String, Integer> redisTemplate
    ) {
        RateLimiter rateLimiter = new CustomRateLimiter(redisTemplate, settings.endpoints.get("ping"));
        FilterRegistrationBean<RateLimitingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RateLimitingFilter(rateLimiter));
        registrationBean.addUrlPatterns("/ping");
        registrationBean.setName("pingRateLimiter");
        return registrationBean;
    }

    @Bean
    @ConditionalOnProperty(name = "custom.enabled", havingValue = "true")
    public FilterRegistrationBean<RateLimitingFilter> pongEndpointWithCustom(
            RedisTemplate<String, Integer> redisTemplate
    ) {
        RateLimiter rateLimiter = new CustomRateLimiter(redisTemplate, settings.endpoints.get("pong"));
        FilterRegistrationBean<RateLimitingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RateLimitingFilter(rateLimiter));
        registrationBean.addUrlPatterns("/pong");
        registrationBean.setName("pongRateLimiter");
        return registrationBean;
    }
}
