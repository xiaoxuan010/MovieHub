package space.astralbridge.spring.moviehub.service;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import space.astralbridge.spring.moviehub.entity.Movie;
import space.astralbridge.spring.moviehub.mapper.MovieMapper;

@Service
public class MovieService extends ServiceImpl<MovieMapper, Movie> {
    /**
     * 根据关键词搜索电影。
     *
     * @param query 搜索关键词 (可以匹配电影标题、演员名、导演名)
     * @param page  分页参数对象
     * @return 分页的电影搜索结果
     */
    public Page<Movie> searchMovies(String query, Page<Movie> page) {
        // 调用 Mapper 层进行数据库搜索
        // getBaseMapper() 返回与此 Service 关联的 Mapper 实例 (MovieMapper)
        return getBaseMapper().searchMovies(page, query);
    }

    public Page<Movie> getMoviesByDirector(Long directorId, Page<Movie> page) {
        return this.baseMapper.selectMoviesByDirector(page, directorId);
    }

    public Page<Movie> getMoviesByActor(Long actorId, Page<Movie> page) {
        return this.baseMapper.selectMoviesByActor(page, actorId);
    }
}
