package space.astralbridge.spring.moviehub.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import space.astralbridge.spring.moviehub.entity.MovieType;
import space.astralbridge.spring.moviehub.mapper.MovieTypeMapper;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MovieTypeService extends ServiceImpl<MovieTypeMapper, MovieType> {

    public MovieType getOrCreateByName(String typeName) {
        MovieType movieType = this.getOne(new QueryWrapper<MovieType>().eq("name", typeName));
        if (movieType == null) {
            movieType = new MovieType();
            movieType.setName(typeName);
            this.save(movieType);
        }
        return movieType;
    }

    public Map<String, MovieType> getOrCreateByNames(Set<String> typeNames) {
        // 1. 批量查询已存在的 MovieType，并转换为 Map
        Map<String, MovieType> existingMovieTypeMap = this.list(new QueryWrapper<MovieType>().in("name", typeNames))
                .stream()
                .collect(Collectors.toMap(MovieType::getName, movieType -> movieType));

        // 2. 找出需要创建的名称并批量创建新的 MovieType
        Set<MovieType> newMovieTypes = typeNames.stream()
                .filter(name -> !existingMovieTypeMap.containsKey(name))
                .map(name -> {
                    MovieType movieType = new MovieType();
                    movieType.setName(name);
                    return movieType;
                })
                .collect(Collectors.toSet());

        if (!newMovieTypes.isEmpty()) {
            getBaseMapper().insert(newMovieTypes);
            newMovieTypes.forEach(movieType -> existingMovieTypeMap.put(movieType.getName(), movieType));
        }

        // 3. 返回按输入名称映射的结果
        return typeNames.stream()
                .collect(Collectors.toMap(name -> name, existingMovieTypeMap::get));
    }

}
