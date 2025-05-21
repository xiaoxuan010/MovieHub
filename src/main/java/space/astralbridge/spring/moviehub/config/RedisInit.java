package space.astralbridge.spring.moviehub.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisInit implements CommandLineRunner {
    private final StringRedisTemplate redisTemplate;

    @Override
    public void run(String... args) throws Exception {
        // 清空 Redis 缓存
        RedisConnectionFactory connectionFactory = redisTemplate.getConnectionFactory();
        if (connectionFactory != null) {
            connectionFactory.getConnection().serverCommands().flushDb();
            log.info("Redis 缓存已清空");
        } else {
            log.warn("Redis 连接工厂未初始化，无法清空缓存");
        }
    }
}