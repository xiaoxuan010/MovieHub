package space.astralbridge.spring.moviehub.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import space.astralbridge.spring.moviehub.entity.Movie;
// 导入新的 Mapper
import space.astralbridge.spring.moviehub.mapper.MovieRankingMapper;
import space.astralbridge.spring.moviehub.service.MovieRankingService;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 电影排行榜服务实现类
 */
@Service
@RequiredArgsConstructor
public class MovieRankingServiceImpl implements MovieRankingService {
    private final MovieRankingMapper movieRankingMapper;
    private static final Set<String> ALLOWED_SORT_FIELDS = new HashSet<>(Arrays.asList("score", "play_count", "release_date", "id"));
    private static final Set<String> ALLOWED_ORDER_DIRECTIONS = new HashSet<>(Arrays.asList("asc", "desc"));

    @Override
    @Cacheable(value = "movies:ranked", key = "#sortBy + ':' + #order + ':' + #current + ':' + #size")
    public Page<Movie> getRankedMovies(String sortBy, String order, Integer current, Integer size) {
        String validSortBy = (sortBy != null && ALLOWED_SORT_FIELDS.contains(sortBy.toLowerCase())) ? sortBy.toLowerCase() : "score";
        String validOrder = (order != null && ALLOWED_ORDER_DIRECTIONS.contains(order.toLowerCase())) ? order.toUpperCase() : "DESC";
        int pageNum = (current != null && current > 0) ? current : 1;
        int pageSize = (size != null && size > 0) ? size : 10;

        Page<Movie> page = new Page<>(pageNum, pageSize);

        Page<Movie> rankedMoviesPage = movieRankingMapper.selectMoviesRanked(page, validSortBy, validOrder);

        return rankedMoviesPage;
    }
}
