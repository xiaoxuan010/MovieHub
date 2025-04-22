package space.astralbridge.spring.moviehub.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import space.astralbridge.spring.moviehub.entity.MovieType;
import space.astralbridge.spring.moviehub.mapper.MovieTypeMapper;

@Service
public class MovieTypeService extends ServiceImpl<MovieTypeMapper, MovieType> {

}
