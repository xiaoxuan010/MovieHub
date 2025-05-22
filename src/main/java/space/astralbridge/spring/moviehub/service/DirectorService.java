package space.astralbridge.spring.moviehub.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import lombok.RequiredArgsConstructor;
import space.astralbridge.spring.moviehub.common.utils.RedisTemplateUtils;
import space.astralbridge.spring.moviehub.entity.Director;
import space.astralbridge.spring.moviehub.mapper.DirectorMapper;

@Service
@RequiredArgsConstructor
public class DirectorService extends ServiceImpl<DirectorMapper, Director> {

    private final StringRedisTemplate redisTemplate;

    private final RedisTemplateUtils redisTemplateUtils;

    @Cacheable(value = "director:name", key = "#directorName")
    public Director getOrCreateByName(String directorName) {
        Director director = this.getOne(new QueryWrapper<Director>().eq("name", directorName));
        if (director == null) {
            director = new Director();
            director.setName(directorName);
            this.save(director);
        }
        return director;
    }

    public Map<String, Director> getOrCreateByNames(Set<String> directorNames) {
        // 1. Batch query existing Directors and convert to Map
        Map<String, Director> existingDirectorMap = this.list(new QueryWrapper<Director>().in("name", directorNames))
                .stream()
                .collect(Collectors.toMap(Director::getName, director -> director));

        // 2. Identify names that need to be created and batch create new Directors
        Set<Director> newDirectors = directorNames.stream()
                .filter(name -> !existingDirectorMap.containsKey(name))
                .map(name -> {
                    Director director = new Director();
                    director.setName(name);
                    return director;
                })
                .collect(Collectors.toSet());

        if (!newDirectors.isEmpty()) {
            getBaseMapper().insert(newDirectors);
            newDirectors.forEach(director -> existingDirectorMap.put(director.getName(), director));
        }

        // 3. Return the result mapped by input names
        return directorNames.stream()
                .collect(Collectors.toMap(name -> name, existingDirectorMap::get));
    }

    /**
     * 根据ID获取导演，添加缓存
     */
    @Override
    @Cacheable(value = "director:id", key = "#id")
    public Director getById(Serializable id) {
        return super.getById(id);
    }

    /**
     * 获取所有导演列表，添加缓存
     */
    @Override
    @Cacheable(value = "directors:all")
    public List<Director> list() {
        return super.list();
    }

    @Override
    public boolean updateById(Director entity) {
        redisTemplateUtils.evictCacheByPrefix("directors:");
        redisTemplate.delete("director:id::" + entity.getId());
        redisTemplate.delete("director:name::" + entity.getName());

        return super.updateById(entity);
    }

    @Override
    public boolean save(Director entity) {
        redisTemplateUtils.evictCacheByPrefix("directors:");

        return super.save(entity);
    }

    @Override
    public boolean removeById(Serializable id) {
        redisTemplateUtils.evictCacheByPrefix("directors:");
        redisTemplate.delete("director:id::" + id);
        redisTemplate.delete("director:name::" + id);

        return super.removeById(id);
    }

}
