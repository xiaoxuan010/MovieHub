package space.astralbridge.spring.moviehub.controller.admin;

import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;
import space.astralbridge.spring.moviehub.common.Result;
import space.astralbridge.spring.moviehub.common.ResultCode;
import space.astralbridge.spring.moviehub.dto.CreateMovieRequest;
import space.astralbridge.spring.moviehub.entity.Movie;
import space.astralbridge.spring.moviehub.service.MovieService;

import java.util.List;

@RestController
@RequestMapping("/api/admin/movie")
@RequiredArgsConstructor
public class AdminMovieController {
    private final ModelMapper modelMapper;
    private final MovieService movieService;

    @GetMapping("/all")
    public Result<List<Movie>> getAllMovies() {
        return Result.success(movieService.list());
    }

    @GetMapping("/")
    public Result<Movie> getMovieById(@PathParam("id") Long id) {
        Movie movie = movieService.getById(id);
        if (movie == null) {
            return Result.fail(ResultCode.VALIDATE_FAILED, "电影不存在");
        }
        return Result.success(movieService.getById(id));
    }

    @PostMapping("/create")
    public Result<Movie> createMovie(@RequestBody CreateMovieRequest createMovieRequest) {
        Movie movie = modelMapper.map(createMovieRequest, Movie.class);

        movieService.save(movie);
        return Result.success(movieService.getById(movie.getId()));
    }

    @PostMapping("/update")
    public Result<Movie> updateMovie(@RequestBody Movie movie) {
        movieService.updateById(movie);
        return Result.success(movieService.getById(movie.getId()));
    }

    @PostMapping("/delete")
    public Result<Void> deleteMovie(@PathParam("mid") Long id) {
        if (movieService.removeById(id)) {
            return Result.success(null);
        }
        return Result.fail(ResultCode.VALIDATE_FAILED, "删除电影失败");

    }

}
