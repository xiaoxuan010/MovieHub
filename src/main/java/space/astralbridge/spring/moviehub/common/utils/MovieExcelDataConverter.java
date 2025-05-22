package space.astralbridge.spring.moviehub.common.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import space.astralbridge.spring.moviehub.dto.MovieExcelData;
import space.astralbridge.spring.moviehub.entity.Actor;
import space.astralbridge.spring.moviehub.entity.Director;
import space.astralbridge.spring.moviehub.entity.Movie;
import space.astralbridge.spring.moviehub.entity.MovieType;
import space.astralbridge.spring.moviehub.service.ActorService;
import space.astralbridge.spring.moviehub.service.DirectorService;
import space.astralbridge.spring.moviehub.service.MovieTypeService;

@Component
@RequiredArgsConstructor
public class MovieExcelDataConverter {

    private final MovieTypeService movieTypeService;
    private final DirectorService directorService;
    private final ActorService actorService;

    public List<Movie> convert(List<MovieExcelData> sourceList) {
        if (sourceList == null || sourceList.isEmpty()) {
            return List.of();
        }

        Set<String> movieTypeNames = new HashSet<>();
        Set<String> directorNames = new HashSet<>();
        Set<String> actorNames = new HashSet<>();

        // Extract all unique names in a single pass
        for (MovieExcelData data : sourceList) {
            Arrays.stream(data.getMovieTypes().split("[,/]"))
                  .map(String::trim)
                  .filter(s -> !s.isEmpty())
                  .forEach(movieTypeNames::add);
                  
            Arrays.stream(data.getDirectors().split("[,/]"))
                  .map(String::trim)
                  .filter(s -> !s.isEmpty())
                  .forEach(directorNames::add);
                  
            Arrays.stream(data.getActors().split("[,/]"))
                  .map(String::trim)
                  .filter(s -> !s.isEmpty())
                  .forEach(actorNames::add);
        }

        Map<String, MovieType> movieTypeMap = movieTypeService.getOrCreateByNames(movieTypeNames);
        Map<String, Director> directorMap = directorService.getOrCreateByNames(directorNames);
        Map<String, Actor> actorMap = actorService.getOrCreateByNames(actorNames);

        return sourceList.stream().map(data -> {
            Movie movie = new Movie();
            movie.setTitle(data.getTitle());
            movie.setDescription(data.getDescription());
            movie.setReleaseDate(data.getReleaseDate());
            movie.setDuration(data.getDuration());
            movie.setCoverImage(data.getCoverImage());
            movie.setRegion(data.getRegion());
            movie.setIsVip(data.getIsVip());
            movie.setScore(data.getScore());
            movie.setVideoUrl("/api/guest/media/" + data.getTitle() + ".mp4");

            if (movieTypeMap != null) {
                movie.setMovieTypes(Arrays.stream(data.getMovieTypes().split("[,/]"))
                    .map(String::trim)
                    .map(movieTypeMap::get)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList()));
            }

            if (directorMap != null) {
                movie.setDirectors(Arrays.stream(data.getDirectors().split("[,/]"))
                    .map(String::trim)
                    .map(directorMap::get)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList()));
            }

            if (actorMap != null) {
                movie.setActors(Arrays.stream(data.getActors().split("[,/]"))
                    .map(String::trim)
                    .map(actorMap::get)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList()));
            }

            return movie;
        }).collect(Collectors.toList());
    }
}
