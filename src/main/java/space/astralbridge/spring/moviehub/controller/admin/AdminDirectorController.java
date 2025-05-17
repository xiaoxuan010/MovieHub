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
import space.astralbridge.spring.moviehub.common.ResultCode;
import space.astralbridge.spring.moviehub.entity.Director;
import space.astralbridge.spring.moviehub.service.DirectorService;

@RestController
@RequestMapping("/admin/director")
@RequiredArgsConstructor
public class AdminDirectorController {
    private final DirectorService directorService;

    @GetMapping
    public Result<List<Director>> getAllDirectors() {
        List<Director> directors = directorService.list();
        if (directors.isEmpty()) {
            return Result.fail(ResultCode.NOT_FOUND, "No directors found");
        } else {
            return Result.success(directors);
        }
    }

    @GetMapping("/{id}")
    public Result<Director> getDirectorById(@PathVariable Long id) {
        Director director = directorService.getById(id);
        if (director == null) {
            return Result.fail(ResultCode.NOT_FOUND, "Director not found");
        } else {
            return Result.success(director);
        }
    }

    @PostMapping
    public Result<Director> createDirector(@RequestBody Director director) {
        if (directorService.save(director)) {
            return Result.success(director);
        } else {
            return Result.fail("Failed to create director");
        }
    }

    @PutMapping("/{id}")
    public Result<Director> updateDirector(@PathVariable Long id, @RequestBody Director director) {
        director.setId(id);
        if (directorService.updateById(director)) {
            return Result.success(director);
        } else {
            return Result.fail(ResultCode.NOT_FOUND, "Director not found or could not be updated");
        }
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteDirector(@PathVariable Long id) {
        if (directorService.removeById(id)) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.NOT_FOUND, "Director not found or could not be deleted");
        }
    }
}
