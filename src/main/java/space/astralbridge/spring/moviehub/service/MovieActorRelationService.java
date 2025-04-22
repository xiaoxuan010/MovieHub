package space.astralbridge.spring.moviehub.service;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import space.astralbridge.spring.moviehub.entity.MovieActorRelation;
import space.astralbridge.spring.moviehub.mapper.MovieActorRelationMapper;

@Service
public class MovieActorRelationService extends ServiceImpl<MovieActorRelationMapper, MovieActorRelation> {

}
