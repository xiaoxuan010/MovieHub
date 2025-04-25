package space.astralbridge.spring.moviehub.controller.guest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import lombok.RequiredArgsConstructor;
import space.astralbridge.spring.moviehub.common.Result;
import space.astralbridge.spring.moviehub.entity.Actor;
import space.astralbridge.spring.moviehub.service.ActorService;

@RestController
@RequestMapping("/api/guest/actors")
@RequiredArgsConstructor
public class ActorController {
    private final ActorService actorService;

    @GetMapping
    public Result<Page<Actor>> getAllActors(@RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size) {
        Page<Actor> actorPage = new Page<>(current, size);
        return Result.success(actorService.page(actorPage));
    }

    @GetMapping("/{id}")
    public Result<Actor> getActorById(@PathVariable Long id) {
        return Result.success(actorService.getById(id));
    }
}
