package com.example.demoratelimiter.web;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisCommands;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

@Configuration
public class RedisConfiguration {

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration("localhost", 6379));
    }

    @Bean
    @ConditionalOnProperty(name = "custom.enabled", havingValue = "true")
    public RedisTemplate<String, Integer> redisTemplate() {
        RedisTemplate<String, Integer> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setValueSerializer(new IntRedisSerializer());
        return redisTemplate;
    }

    static class IntRedisSerializer implements RedisSerializer<Integer> {

        @Override
        public byte[] serialize(Integer integer) throws SerializationException {
            return integer.toString().getBytes();
        }

        @Override
        public Integer deserialize(byte[] bytes) throws SerializationException {
            if (bytes == null) {
                return null;
            }
            return Integer.valueOf(new String(bytes));
        }
    }
    @Bean
    @ConditionalOnProperty(name = "library.enabled", havingValue = "true")
    public RedisCommands redisCommands(LettuceConnectionFactory lettuceConnectionFactory) {
        return lettuceConnectionFactory.getConnection();
    }
}
