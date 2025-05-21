package space.astralbridge.spring.moviehub.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import jakarta.annotation.Resource;
import space.astralbridge.spring.moviehub.common.utils.FastJson2RedisSerializer;

@Configuration
public class RedisConfig {
    @Resource
    private RedisConnectionFactory redisConnectionFactory;

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        FastJson2RedisSerializer<Object> fastJson2RedisSerializer = new FastJson2RedisSerializer<>(Object.class);
        StringRedisSerializer serializer = new StringRedisSerializer();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(serializer);
        template.setHashKeySerializer(serializer);
        template.setValueSerializer(fastJson2RedisSerializer);
        template.setHashValueSerializer(fastJson2RedisSerializer);
        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public RedisCacheConfiguration cacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new FastJson2RedisSerializer<>(Object.class)))
                .disableCachingNullValues();
    }
}
