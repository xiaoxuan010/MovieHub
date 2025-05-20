package space.astralbridge.spring.moviehub.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import space.astralbridge.spring.moviehub.entity.Movie;

@Mapper
public interface MovieRankingMapper extends BaseMapper<Movie> { // 继承 BaseMapper<Movie>

    /**
     * 根据指定条件和排序规则分页查询电影列表 (排行榜专用)
     *
     * @param page   分页对象，包含当前页、每页大小等信息
     * @param sortBy 排序字段 (例如 "score", "play_count", "release_date")
     * @param order  排序顺序 ("ASC" 或 "DESC")
     * @return 分页后的电影列表
     */
    Page<Movie> selectMoviesRanked(Page<Movie> page, @Param("sortBy") String sortBy, @Param("order") String order);

}
