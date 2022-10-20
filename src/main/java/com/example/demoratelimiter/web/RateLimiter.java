package com.example.demoratelimiter.web;

public interface RateLimiter {

    boolean canProcessRequest(String key);
}
