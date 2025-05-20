package space.astralbridge.spring.moviehub.controller.guest;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import space.astralbridge.spring.moviehub.common.Result;
import space.astralbridge.spring.moviehub.common.ResultCode;
import space.astralbridge.spring.moviehub.entity.MovieType;
import space.astralbridge.spring.moviehub.service.MovieTypeService;

@RestController
@RequestMapping("/api/guest/movie-types")
@RequiredArgsConstructor
public class MovieTypeController {
    private final MovieTypeService movieTypeService;

    @GetMapping
    public Result<List<MovieType>> getAllMovieTypes() {
        List<MovieType> movieTypes = movieTypeService.list();
        return Result.success(movieTypes);
    }

    @GetMapping("/{id}")
    public Result<MovieType> getMovieTypeById(@PathVariable Long id) {
        MovieType movieType = movieTypeService.getById(id);
        if (movieType == null) {
            return Result.fail(ResultCode.NOT_FOUND, "Movie type not found");
        }
        return Result.success(movieType);
    }
}
