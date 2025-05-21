package space.astralbridge.spring.moviehub.config;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    public ModelMapper modelMapper() {
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

        // 添加自定义转换器：List<MovieExcelData> -> List<Movie>
        Converter<List<MovieExcelData>, List<Movie>> movieExcelDataListToMovieListConverter = context -> {
            List<MovieExcelData> source = context.getSource();
            if (source == null || source.isEmpty()) {
                return null;
            }
            Set<String> movieTypeNames = new HashSet<>();
            Set<String> directorNames = new HashSet<>();
            Set<String> actorNames = new HashSet<>();
            source.forEach(movieExcelData -> {
                movieTypeNames.addAll(Arrays.asList(movieExcelData.getMovieTypes().split("[,/]")));
                directorNames.addAll(Arrays.asList(movieExcelData.getDirectors().split("[,/]")));
                actorNames.addAll(Arrays.asList(movieExcelData.getActors().split("[,/]")));
            });

            Map<String, MovieType> movieTypeMap = movieTypeService
                    .getOrCreateByNames(movieTypeNames);
            Map<String, Director> directorMap = directorService.getOrCreateByNames(directorNames);
            Map<String, Actor> actorMap = actorService.getOrCreateByNames(actorNames);

            return source.stream().map(movieExcelData -> {
                Movie movie = new Movie();
                movie.setTitle(movieExcelData.getTitle());
                movie.setDescription(movieExcelData.getDescription());
                movie.setReleaseDate(movieExcelData.getReleaseDate());
                movie.setDuration(movieExcelData.getDuration());
                movie.setCoverImage(movieExcelData.getCoverImage());
                movie.setRegion(movieExcelData.getRegion());
                movie.setIsVip(movieExcelData.getIsVip());
                movie.setScore(movieExcelData.getScore());

                // 设置默认的资源路径
                movie.setVideoUrl("/api/guest/media/" + movie.getTitle() + ".mp4");

                // 设置电影类型、导演和演员
                if (movieTypeMap != null) {
                    movie.setMovieTypes(Arrays.stream(movieExcelData.getMovieTypes().split("[,/]"))
                            .map(type -> movieTypeMap.get(type.trim()))
                            .collect(Collectors.toList()));
                }
                if (directorMap != null) {
                    movie.setDirectors(Arrays.stream(movieExcelData.getDirectors().split("[,/]"))
                            .map(type -> directorMap.get(type.trim()))
                            .collect(Collectors.toList()));
                }
                if (actorMap != null) {
                    movie.setActors(Arrays.stream(movieExcelData.getActors().split("[,/]"))
                            .map(type -> actorMap.get(type.trim()))
                            .collect(Collectors.toList()));
                }
                return movie;
            }).collect(Collectors.toList());

        };

        // 注册自定义转换器
        modelMapper.addConverter(movieExcelDataListToMovieListConverter);

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
