package space.astralbridge.spring.moviehub.controller.admin;

import java.util.List;

import org.modelmapper.ModelMapper;
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
import space.astralbridge.spring.moviehub.dto.CreateActorRequestDto;
import space.astralbridge.spring.moviehub.entity.Actor;
import space.astralbridge.spring.moviehub.service.ActorService;

@RestController
@RequestMapping("/admin/actor")
@RequiredArgsConstructor
public class AdminActorController {

    private final ModelMapper modelMapper;
    private final ActorService actorService;

    @GetMapping
    public Result<List<Actor>> getAllActors() {
        List<Actor> actors = actorService.list();
        if (actors.isEmpty()) {
            return Result.fail(ResultCode.NOT_FOUND, "No actors found");
        } else {
            return Result.success(actors);
        }

    }

    @GetMapping("/{id}")
    public Result<Actor> getActorById(@PathVariable Long id) {
        Actor actor = actorService.getById(id);
        if (actor == null) {
            return Result.fail(ResultCode.NOT_FOUND, "Actor not found");
        } else {
            return Result.success(actor);
        }
    }

    @PostMapping
    public Result<Actor> createActor(@RequestBody CreateActorRequestDto dto) {
        Actor actor = modelMapper.map(dto, Actor.class);
        if (actorService.save(actor)) {
            return Result.success(actor);
        } else {
            return Result.fail("Failed to create actor");
        }
    }

    @PutMapping("/{id}")
    public Result<Actor> updateActor(@PathVariable Long id, @RequestBody Actor actor) {
        actor.setId(id);
        // return actorService.updateById(actor);
        if (actorService.updateById(actor)) {
            return Result.success(actor);
        } else {
            return Result.fail(ResultCode.NOT_FOUND, "Actor not found or could not be updated");
        }
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteActor(@PathVariable Long id) {
        if (actorService.removeById(id)) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.NOT_FOUND, "Actor not found or could not be deleted");
        }
    }

}
