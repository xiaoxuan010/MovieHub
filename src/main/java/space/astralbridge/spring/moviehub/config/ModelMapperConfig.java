package space.astralbridge.spring.moviehub.config;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;
import space.astralbridge.spring.moviehub.dto.MovieExcelData;
import space.astralbridge.spring.moviehub.entity.Actor;
import space.astralbridge.spring.moviehub.entity.Director;
import space.astralbridge.spring.moviehub.entity.Movie;
import space.astralbridge.spring.moviehub.entity.MovieType;
import space.astralbridge.spring.moviehub.service.ActorService;
import space.astralbridge.spring.moviehub.service.DirectorService;
import space.astralbridge.spring.moviehub.service.MovieTypeService;

@Configuration
@RequiredArgsConstructor
public class ModelMapperConfig {
    private final MovieTypeService movieTypeService;
    private final DirectorService directorService;
    private final ActorService actorService;

    @Bean
    ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // 自定义转换器：String -> List<MovieType>
        Converter<String, List<MovieType>> stringToMovieTypeListConverter = context -> {
            String source = context.getSource();
            if (source == null || source.isEmpty()) {
                return null;
            }
            return Arrays.stream(source.split("[,/]"))
                    .map(type -> {
                        return movieTypeService.getOrCreateByName(type.trim());
                    })
                    .collect(Collectors.toList());
        };

        // 定义转换为：String -> List<Director>
        Converter<String, List<Director>> stringToDirectorListConverter = context -> {
            String source = context.getSource();
            if (source == null || source.isEmpty()) {
                return null;
            }
            return Arrays.stream(source.split("[,/]"))
                    .map(type -> {
                        return directorService.getOrCreateByName(type.trim());
                    })
                    .collect(Collectors.toList());
        };

        // 定义转换为：String -> List<Actor>
        Converter<String, List<Actor>> stringToActorListConverter = context -> {
            String source = context.getSource();
            if (source == null || source.isEmpty()) {
                return null;
            }
            return Arrays.stream(source.split("[,/]"))
                    .map(type -> {
                        return actorService.getOrCreateByName(type.trim());
                    })
                    .collect(Collectors.toList());
        };

        // 添加自定义映射规则
        modelMapper.typeMap(MovieExcelData.class, Movie.class)
                .addMappings(mapper -> mapper.using(stringToMovieTypeListConverter)
                        .map(MovieExcelData::getMovieTypes, Movie::setMovieTypes))
                .addMappings(mapper -> mapper.using(stringToDirectorListConverter)
                        .map(MovieExcelData::getDirectors, Movie::setDirectors))
                .addMappings(mapper -> mapper.using(stringToActorListConverter)
                        .map(MovieExcelData::getActors, Movie::setActors));

        return modelMapper;
    }
}
