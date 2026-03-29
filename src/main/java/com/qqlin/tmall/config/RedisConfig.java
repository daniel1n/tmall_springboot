package com.qqlin.tmall.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * Redis 缓存配置类
 *
 * @author qqlin
 * @since 2020-6-2
 */
@Configuration
public class RedisConfig extends CachingConfigurerSupport {

  @Bean
  public CacheManager cacheManager(RedisTemplate<?, ?> redisTemplate) {
    final StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
    final Jackson2JsonRedisSerializer jackson2JsonRedisSerializer =
        new Jackson2JsonRedisSerializer(Object.class);
    final ObjectMapper objectMapper = new ObjectMapper();

    objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.PUBLIC_ONLY);
    objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance,
            ObjectMapper.DefaultTyping.NON_FINAL,
            JsonTypeInfo.As.PROPERTY);
    jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
    redisTemplate.setKeySerializer(stringRedisSerializer);
    redisTemplate.setHashKeySerializer(stringRedisSerializer);

    redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
    redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);

    RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofHours(1));

    RedisCacheManager redisCacheManager = RedisCacheManager.builder(RedisCacheWriter.lockingRedisCacheWriter(redisTemplate.getConnectionFactory()))
            .cacheDefaults(cacheConfiguration)
            .build();
    return redisCacheManager;
  }
}
