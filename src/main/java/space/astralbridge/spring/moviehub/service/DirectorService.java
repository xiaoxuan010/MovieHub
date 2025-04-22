package space.astralbridge.spring.moviehub.service;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import space.astralbridge.spring.moviehub.entity.Director;
import space.astralbridge.spring.moviehub.mapper.DirectorMapper;

@Service
public class DirectorService extends ServiceImpl<DirectorMapper, Director> {

}
