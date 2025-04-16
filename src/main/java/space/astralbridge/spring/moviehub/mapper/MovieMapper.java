package space.astralbridge.spring.moviehub.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import space.astralbridge.spring.moviehub.entity.Movie;

@Mapper
public interface MovieMapper extends BaseMapper<Movie> {

}
