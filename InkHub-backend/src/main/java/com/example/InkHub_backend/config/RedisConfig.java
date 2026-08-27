package com.example.InkHub_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

// Redis 序列化配置：key 用 String，value 用 JSON，避免乱码
// ⚠️ Spring Data Redis 4.0 起 GenericJackson2JsonRedisSerializer 已弃用（将被移除），
//    改用 GenericJacksonJsonRedisSerializer（去掉"2"，基于 Jackson 3，Spring Boot 4 自带）
@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        // 新写法：builder() 用 Jackson 3 的 JsonMapper 创建，行为与旧版一致（JSON 带类型信息，可还原对象）
        GenericJacksonJsonRedisSerializer jsonSerializer = GenericJacksonJsonRedisSerializer.builder().build();
        template.setValueSerializer(jsonSerializer);
        template.setHashValueSerializer(jsonSerializer);
        return template;
    }
}
