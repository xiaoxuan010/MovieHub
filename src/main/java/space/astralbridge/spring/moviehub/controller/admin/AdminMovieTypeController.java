package space.astralbridge.spring.moviehub.controller.admin;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import space.astralbridge.spring.moviehub.common.Result;
import space.astralbridge.spring.moviehub.entity.MovieType;
import space.astralbridge.spring.moviehub.service.MovieTypeService;

@RestController
@RequestMapping("/admin/movie-type")
@RequiredArgsConstructor
public class AdminMovieTypeController {
    private final MovieTypeService movieTypeService;

    @GetMapping
    public Result<List<MovieType>> getAllMovieTypes() {
        List<MovieType> movieTypes = movieTypeService.list();
        if (movieTypes.isEmpty()) {
            return Result.fail("No movie types found");
        } else {
            return Result.success(movieTypes);
        }
    }

    @GetMapping("/{id}")
    public Result<MovieType> getMovieTypeById(@PathVariable Long id) {
        MovieType movieType = movieTypeService.getById(id);
        if (movieType == null) {
            return Result.fail("Movie type not found");
        } else {
            return Result.success(movieType);
        }
    }

    @PostMapping
    public Result<MovieType> createMovieType(@RequestBody MovieType movieType) {
        if (movieTypeService.save(movieType)) {
            return Result.success(movieType);
        } else {
            return Result.fail("Failed to create movie type");
        }
    }

    @PutMapping("/{id}")
    public Result<MovieType> updateMovieType(@PathVariable Long id, @RequestBody MovieType movieType) {
        movieType.setId(id);
        if (movieTypeService.updateById(movieType)) {
            return Result.success(movieType);
        } else {
            return Result.fail("Failed to update movie type");
        }
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteMovieType(@PathVariable Long id) {
        if (movieTypeService.removeById(id)) {
            return Result.success();
        } else {
            return Result.fail("Failed to delete movie type");
        }
    }
}
