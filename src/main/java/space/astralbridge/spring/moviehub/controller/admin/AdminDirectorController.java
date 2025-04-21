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
import space.astralbridge.spring.moviehub.entity.Director;
import space.astralbridge.spring.moviehub.service.DirectorService;

@RestController
@RequestMapping("/admin/director")
@RequiredArgsConstructor
public class AdminDirectorController {
    private final DirectorService directorService;

    @GetMapping
    public List<Director> getAllDirectors() {
        return directorService.list();
    }

    @GetMapping("/{id}")
    public Director getDirectorById(@PathVariable Long id) {
        return directorService.getById(id);
    }

    @PostMapping
    public boolean createDirector(@RequestBody Director director) {
        return directorService.save(director);
    }

    @PutMapping("/{id}")
    public boolean updateDirector(@PathVariable Long id, @RequestBody Director director) {
        director.setId(id);
        return directorService.updateById(director);
    }

    @DeleteMapping("/{id}")
    public boolean deleteDirector(@PathVariable Long id) {
        return directorService.removeById(id);
    }
}
