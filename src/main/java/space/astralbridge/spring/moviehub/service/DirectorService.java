package space.astralbridge.spring.moviehub.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import space.astralbridge.spring.moviehub.entity.Director;
import space.astralbridge.spring.moviehub.mapper.DirectorMapper;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DirectorService extends ServiceImpl<DirectorMapper, Director> {
    public Director getOrCreateByName(String directorName) {
        Director director = this.getOne(new QueryWrapper<Director>().eq("name", directorName));
        if (director == null) {
            director = new Director();
            director.setName(directorName);
            this.save(director);
        }
        return director;
    }

    public Map<String, Director> getOrCreateByNames(Set<String> directorNames) {
        // 1. Batch query existing Directors and convert to Map
        Map<String, Director> existingDirectorMap = this.list(new QueryWrapper<Director>().in("name", directorNames))
                .stream()
                .collect(Collectors.toMap(Director::getName, director -> director));

        // 2. Identify names that need to be created and batch create new Directors
        Set<Director> newDirectors = directorNames.stream()
                .filter(name -> !existingDirectorMap.containsKey(name))
                .map(name -> {
                    Director director = new Director();
                    director.setName(name);
                    return director;
                })
                .collect(Collectors.toSet());

        if (!newDirectors.isEmpty()) {
            getBaseMapper().insert(newDirectors);
            newDirectors.forEach(director -> existingDirectorMap.put(director.getName(), director));
        }

        // 3. Return the result mapped by input names
        return directorNames.stream()
                .collect(Collectors.toMap(name -> name, existingDirectorMap::get));
    }

}
