package space.astralbridge.spring.moviehub.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import space.astralbridge.spring.moviehub.entity.Movie;

public interface MovieRankingService {

    /**
     * 获取电影排行榜
     *
     * @param sortBy    排序字段
     * @param order     排序顺序
     * @param current   当前页码
     * @param size      每页大小
     * @return 分页后的电影排行榜数据
     */
    Page<Movie> getRankedMovies(String sortBy, String order, Integer current, Integer size);
}
