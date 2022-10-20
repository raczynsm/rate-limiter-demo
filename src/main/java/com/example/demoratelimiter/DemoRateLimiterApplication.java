package com.example.demoratelimiter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class DemoRateLimiterApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoRateLimiterApplication.class, args);
	}

}
