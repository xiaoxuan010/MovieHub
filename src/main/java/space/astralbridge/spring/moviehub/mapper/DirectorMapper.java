package space.astralbridge.spring.moviehub.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import space.astralbridge.spring.moviehub.entity.Director;

@Mapper
public interface DirectorMapper extends BaseMapper<Director> {

}
