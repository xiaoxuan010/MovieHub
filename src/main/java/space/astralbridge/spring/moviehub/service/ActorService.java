package space.astralbridge.spring.moviehub.service;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import space.astralbridge.spring.moviehub.entity.Actor;
import space.astralbridge.spring.moviehub.mapper.ActorMapper;

@Service
public class ActorService extends ServiceImpl<ActorMapper, Actor> {

}
