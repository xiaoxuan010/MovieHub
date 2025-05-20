package space.astralbridge.spring.moviehub.service;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import space.astralbridge.spring.moviehub.entity.Actor;
import space.astralbridge.spring.moviehub.mapper.ActorMapper;

@Service
public class ActorService extends ServiceImpl<ActorMapper, Actor> {
    public Actor getOrCreateByName(String actorName) {
        Actor actor = this.getOne(new QueryWrapper<Actor>().eq("name", actorName));
        if (actor == null) {
            actor = new Actor();
            actor.setName(actorName);
            this.save(actor);
        }
        return actor;
    }

    public Map<String, Actor> getOrCreateByNames(Set<String> actorNames) {
        // 1. 批量查询已存在的 Actor，并转换为 Map
        Map<String, Actor> existingActorMap = this.list(new QueryWrapper<Actor>().in("name", actorNames))
                .stream()
                .collect(Collectors.toMap(Actor::getName, actor -> actor));

        // 2. 找出需要创建的名称并批量创建新的 Actor
        Set<Actor> newActors = actorNames.stream()
                .filter(name -> !existingActorMap.containsKey(name))
                .map(name -> {
                    Actor actor = new Actor();
                    actor.setName(name);
                    return actor;
                })
                .collect(Collectors.toSet());

        if (!newActors.isEmpty()) {
            getBaseMapper().insert(newActors);
            newActors.forEach(actor -> existingActorMap.put(actor.getName(), actor));
        }

        // 3. 返回按输入名称映射的结果
        return actorNames.stream()
                .collect(Collectors.toMap(name -> name, existingActorMap::get));
    }
}
