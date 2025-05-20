package space.astralbridge.spring.moviehub.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import space.astralbridge.spring.moviehub.entity.Movie;

@Mapper
public interface MovieMapper extends BaseMapper<Movie> {
    List<Map<String, Object>> countMoviesByType();

    /**
     * 获取播放量最高的N部电影
     * @param limit 限制返回的电影数量
     * @return 包含电影标题和播放量的Map列表
     */
    List<Map<String, Object>> selectTopMoviesByPlayCount(Integer limit);
    
    /**
     * 获取播放量最高的N部电影的完整信息
     * @param limit 限制返回的电影数量
     * @return 按播放量降序排列的电影对象列表
     */
    List<Movie> selectTopNMoviesByPlayCount(Integer limit);
    
    /**
     * 根据条件查询电影列表
     * @param region 地区
     * @param typeId 类型ID
     * @return 电影列表
     */
    List<Movie> findMoviesByCondition(@Param("region") String region, @Param("typeId") Long typeId);

    /**
     * 根据关键词搜索电影（标题、演员名、导演名）并进行分页。
     * @param page 分页对象，MybatisPlus会自动处理分页逻辑
     * @param query 搜索关键词
     * @return 分页后的电影列表
     */
    Page<Movie> searchMovies(Page<Movie> page, @Param("query") String query);

    /**
     * 根据导演ID筛选电影并进行分页。
     * 
     * @param page       分页对象，MybatisPlus会自动处理分页逻辑
     * @param directorId 导演ID
     * @return 分页后的电影列表
     */
    Page<Movie> selectMoviesByDirector(Page<Movie> page, @Param("directorId") Long directorId);

    /**
     * 根据演员ID筛选电影并进行分页。
     * 
     * @param page    分页对象，MybatisPlus会自动处理分页逻辑
     * @param actorId 演员ID
     * @return 分页后的电影列表
     */
    Page<Movie> selectMoviesByActor(Page<Movie> page, @Param("actorId") Long actorId);

    /**
     * 根据类型ID筛选电影并进行分页。
     * 
     * @param moviePage 分页对象，MybatisPlus会自动处理分页逻辑
     * @param typeId    类型ID
     * @return 分页后的电影列表
     */
    Page<Movie> selectMoviesByType(Page<Movie> moviePage, @Param("typeId") Long typeId);

    /**
     * 根据VIP类型筛选电影并进行分页。
     * 
     * @param moviePage 分页对象，MybatisPlus会自动处理分页逻辑
     * @param region    地区
     * @return 分页后的电影列表
     */
    Page<Movie> selectMoviesByVipType(Page<Movie> moviePage, Long vipType);

}
