package space.astralbridge.spring.moviehub.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import space.astralbridge.spring.moviehub.entity.Movie;
import space.astralbridge.spring.moviehub.mapper.MovieMapper;

@Service
public class MovieService extends ServiceImpl<MovieMapper, Movie> {

}
