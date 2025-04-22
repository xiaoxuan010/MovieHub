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
import space.astralbridge.spring.moviehub.entity.Actor;
import space.astralbridge.spring.moviehub.service.ActorService;

@RestController
@RequestMapping("/admin/actor")
@RequiredArgsConstructor
public class AdminActorController {
    private final ActorService actorService;

    @GetMapping
    public List<Actor> getAllActors() {
        return actorService.list();
    }

    @GetMapping("/{id}")
    public Actor getActorById(@PathVariable Long id) {
        return actorService.getById(id);
    }

    @PostMapping
    public boolean createActor(@RequestBody Actor actor) {
        return actorService.save(actor);
    }

    @PutMapping("/{id}")
    public boolean updateActor(@PathVariable Long id, @RequestBody Actor actor) {
        actor.setId(id);
        return actorService.updateById(actor);
    }

    @DeleteMapping("/{id}")
    public boolean deleteActor(@PathVariable Long id) {
        return actorService.removeById(id);
    }

}
