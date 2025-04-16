package space.astralbridge.spring.moviehub.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import space.astralbridge.spring.moviehub.entity.MovieType;

import java.util.List;

@Mapper
public interface MovieTypeMapper extends BaseMapper<MovieType> {
    List<MovieType> selectTypesByMovieId(Long movieId);
}
