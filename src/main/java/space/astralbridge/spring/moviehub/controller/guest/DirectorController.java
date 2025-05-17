package space.astralbridge.spring.moviehub.controller.guest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import lombok.RequiredArgsConstructor;
import space.astralbridge.spring.moviehub.common.Result;
import space.astralbridge.spring.moviehub.entity.Director;
import space.astralbridge.spring.moviehub.service.DirectorService;

@RestController
@RequestMapping("/api/guest/directors")
@RequiredArgsConstructor
public class DirectorController {
    private final DirectorService directorService;

    @GetMapping
    public Result<Page<Director>> getAllDirectors(@RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size) {
        Page<Director> directorPage = new Page<>(current, size);
        return Result.success(directorService.page(directorPage));
    }

    @GetMapping("/{id}")
    public Result<Director> getDirectorById(@PathVariable Long id) {
        return Result.success(directorService.getById(id));
    }
}
