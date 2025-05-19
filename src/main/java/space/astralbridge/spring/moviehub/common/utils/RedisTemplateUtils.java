package space.astralbridge.spring.moviehub.common.utils;

import java.util.Set;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RedisTemplateUtils {

    private final StringRedisTemplate redisTemplate;

    /**
     * 删除所有以指定前缀开头的 Redis 缓存 key
     * 
     * @param prefix 例如："movies:"
     */
    public void evictCacheByPrefix(String prefix) {
        Set<String> keys = redisTemplate.keys(prefix + "*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }
}
