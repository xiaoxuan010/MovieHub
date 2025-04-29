package space.astralbridge.spring.moviehub.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import space.astralbridge.spring.moviehub.entity.Movie;

import java.util.List;
import java.util.Map;

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
}
