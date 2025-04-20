package space.astralbridge.spring.moviehub.mapper;

import org.apache.ibatis.annotations.Mapper;

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
}
