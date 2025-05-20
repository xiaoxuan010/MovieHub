package space.astralbridge.spring.moviehub.service;

import java.io.Serializable;
import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import lombok.RequiredArgsConstructor;
import space.astralbridge.spring.moviehub.common.utils.RedisTemplateUtils;
import space.astralbridge.spring.moviehub.entity.Movie;
import space.astralbridge.spring.moviehub.mapper.MovieMapper;

@Service
@RequiredArgsConstructor
public class MovieService extends ServiceImpl<MovieMapper, Movie> {
    private final StringRedisTemplate redisTemplate;
    private final RedisTemplateUtils redisTemplateUtils;

    /**
     * 根据关键词搜索电影。
     *
     * @param query 搜索关键词 (可以匹配电影标题、演员名、导演名)
     * @param page  分页参数对象
     * @return 分页的电影搜索结果
     */
    @Cacheable(value = "movies:search", key = "#query + ':' + #page.current + ':' + #page.size")
    public Page<Movie> searchMovies(String query, Page<Movie> page) {
        // 调用 Mapper 层进行数据库搜索
        // getBaseMapper() 返回与此 Service 关联的 Mapper 实例 (MovieMapper)
        return getBaseMapper().searchMovies(page, query);
    }

    @Cacheable(value = "movies:director", key = "#directorId + ':' + #page.current + ':' + #page.size")
    public Page<Movie> getMoviesByDirector(Long directorId, Page<Movie> page) {
        return this.baseMapper.selectMoviesByDirector(page, directorId);
    }

    @Cacheable(value = "movies:actor", key = "#actorId + ':' + #page.current + ':' + #page.size")
    public Page<Movie> getMoviesByActor(Long actorId, Page<Movie> page) {
        return this.baseMapper.selectMoviesByActor(page, actorId);
    }

    @Cacheable(value = "movies:type", key = "#typeId + ':' + #page.current + ':' + #page.size")
    public Page<Movie> getMoviesByType(Long typeId, Page<Movie> page) {
        return this.baseMapper.selectMoviesByType(page, typeId);
    }

    @Cacheable(value = "movies:vip", key = "#vipType + ':' + #page.current + ':' + #page.size")
    public Page<Movie> getMoviesByVipType(Long vipType, Page<Movie> page) {
        return this.baseMapper.selectMoviesByVipType(page, vipType);
    }

    /**
     * 根据ID获取电影，添加缓存
     */
    @Override
    @Cacheable(value = "movie:id", key = "#id")
    public Movie getById(Serializable id) {
        return super.getById(id);
    }

    /**
     * 获取所有电影列表，添加缓存
     */
    @Override
    @Cacheable(value = "movies:all")
    public List<Movie> list() {
        return super.list();
    }

    @Override
    public boolean updateById(Movie entity) {
        redisTemplate.delete("movie:id::" + entity.getId());
        redisTemplateUtils.evictCacheByPrefix("movies:");
        return super.updateById(entity);
    }

    @Override
    public boolean save(Movie entity) {
        redisTemplateUtils.evictCacheByPrefix("movies:");
        return super.save(entity);
    }

    @Override
    @Cacheable(value = "movies:page", key = "#page.current + ':' + #page.size")
    public <E extends IPage<Movie>> E page(E page) {
        return super.page(page);
    }

}
