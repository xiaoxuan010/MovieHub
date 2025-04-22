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
import space.astralbridge.spring.moviehub.entity.MovieType;
import space.astralbridge.spring.moviehub.service.MovieTypeService;

@RestController
@RequestMapping("/admin/movie-type")
@RequiredArgsConstructor
public class AdminMovieTypeController {
    private final MovieTypeService movieTypeService;

    @GetMapping
    public List<MovieType> getAllMovieTypes() {
        return movieTypeService.list();
    }

    @GetMapping("/{id}")
    public MovieType getMovieTypeById(@PathVariable Long id) {
        return movieTypeService.getById(id);
    }

    @PostMapping
    public boolean createMovieType(@RequestBody MovieType movieType) {
        return movieTypeService.save(movieType);
    }

    @PutMapping("/{id}")
    public boolean updateMovieType(@PathVariable Long id, @RequestBody MovieType movieType) {
        movieType.setId(id);
        return movieTypeService.updateById(movieType);
    }

    @DeleteMapping("/{id}")
    public boolean deleteMovieType(@PathVariable Long id) {
        return movieTypeService.removeById(id);
    }
}
