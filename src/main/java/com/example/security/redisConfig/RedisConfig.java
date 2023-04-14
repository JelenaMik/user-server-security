package com.example.security.redisConfig;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.*;
import springfox.documentation.spring.web.json.Json;

import java.time.Duration;

@Configuration
@EnableCaching
public class RedisConfig {





//    public RedisCacheConfiguration cacheConfiguration() {
//        RedisSerializationContext.SerializationPair<Object> serializationPair =
//                RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer());
//        RedisSerializationContext.SerializationPair<String> stringSerializationPair =
//                RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer());
//        return RedisCacheConfiguration.defaultCacheConfig()
//                .entryTtl(Duration.ofMinutes(60))
//                .disableCachingNullValues()
//                .serializeKeysWith(stringSerializationPair)
//                .serializeValuesWith(serializationPair);
//    }



@Bean
    public JedisConnectionFactory jedisConnectionFactory(){
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName("127.0.0.1");
        redisStandaloneConfiguration.setPort(6379);
//        redisStandaloneConfiguration.setDatabase(1);
//        redisStandaloneConfiguration.setPassword("password");

        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(redisStandaloneConfiguration);
        return jedisConnectionFactory;
    }

//    public RedisCacheManager redisCacheManager() {
//        RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
//                .disableCachingNullValues()
//                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new JdkSerializationRedisSerializer()))
//                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()));
//        RedisCacheManager redisCacheManager = RedisCacheManager.builder(jedisConnectionFactory())
//                .cacheDefaults(cacheConfiguration)
//
//                .transactionAware()
//                .build();
//        return redisCacheManager;
//    }

//
//    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
//        RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
//                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
//
//        return RedisCacheManager.builder(redisConnectionFactory)
//                .cacheDefaults(cacheConfiguration)
//                .build();
//    }





@Bean

    public RedisCacheConfiguration cacheConfiguration() {
    return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(60))
                .disableCachingNullValues()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new Jackson2JsonRedisSerializer<Object>(Object.class)))
//            .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new JdkSerializationRedisSerializer()));
    }


@Bean
    public RedisTemplate<String, Object> redisTemplate(){
        RedisTemplate<String, Object> redisTemplate =new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new JdkSerializationRedisSerializer());
        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<Object>(Object.class));
        redisTemplate.setEnableTransactionSupport(true);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }


//    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
//        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
//        redisTemplate.setConnectionFactory(redisConnectionFactory);
//
//        // Set up a custom serializer for values
//        RedisSerializer<Object> serializer = new GenericJackson2JsonRedisSerializer();
//        redisTemplate.setValueSerializer(serializer);
//        redisTemplate.setHashValueSerializer(serializer);
//
//        // Use the StringRedisSerializer for keys
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
//
//        redisTemplate.afterPropertiesSet();
//        return redisTemplate;
//
//
//    }

}
