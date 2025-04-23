package com.skthvl.cinemetrics.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

@Configuration
public class CacheConfig {

  @Value("${cinemetrics.cache.expiration-in-minutes}")
  private int expirationTime;

  @Bean
  public RedisCacheManager cacheManager(final RedisConnectionFactory connectionFactory) {
    final ObjectMapper objectMapper =
        new ObjectMapper()
            .activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);

    final Jackson2JsonRedisSerializer<Object> jsonSerializer =
        new Jackson2JsonRedisSerializer<>(objectMapper, Object.class);

    final RedisSerializationContext.SerializationPair<Object> pair =
        RedisSerializationContext.SerializationPair.fromSerializer(jsonSerializer);

    final RedisCacheConfiguration config =
        RedisCacheConfiguration.defaultCacheConfig()
            .serializeValuesWith(pair)
            .entryTtl(Duration.ofMinutes(expirationTime))
            .disableCachingNullValues();

    return RedisCacheManager.builder(connectionFactory).cacheDefaults(config).build();
  }
}
