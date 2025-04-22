package space.astralbridge.spring.moviehub.service;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import space.astralbridge.spring.moviehub.entity.MovieDirectorRelation;
import space.astralbridge.spring.moviehub.mapper.MovieDirectorRelationMapper;

@Service
public class MovieDirectorRelationService extends ServiceImpl<MovieDirectorRelationMapper, MovieDirectorRelation> {

}
