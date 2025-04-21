package space.astralbridge.spring.moviehub.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;

import space.astralbridge.spring.moviehub.entity.Actor;

@MybatisPlusTest
@Transactional
public class ActorMapperTest {
    @Autowired
    private ActorMapper actorMapper;

    public void assertActor1(Actor actor) {
        System.out.println(actor);

        assertNotNull(actor);
        assertEquals("张三", actor.getName());
        assertEquals("/images/actors/zhangsan.jpg", actor.getPhoto());
        assertEquals("著名演员张三", actor.getDescription());
    }

    @Test
    public void testSelectById() {
        Actor actor = actorMapper.selectById(1L);
        assertActor1(actor);
    }

    @Test
    public void testSelectByIdNotFound() {
        Actor actor = actorMapper.selectById(999L);
        assertEquals(null, actor);
    }

    @Test
    public void testSelectList() {
        List<Actor> actors = actorMapper.selectList(null);
        assertNotNull(actors);
        assertEquals(3, actors.size());
        assertActor1(actors.get(0));
    }

    @Test
    public void testInsert() {
        Actor actor = new Actor();
        actor.setName("李四");
        actor.setPhoto("/images/actors/lisi.jpg");
        actor.setDescription("著名演员李四");

        int result = actorMapper.insert(actor);
        assertEquals(1, result);

        Actor insertedActor = actorMapper.selectById(actor.getId());
        assertNotNull(insertedActor);
        assertEquals("李四", insertedActor.getName());
        assertEquals("/images/actors/lisi.jpg", insertedActor.getPhoto());
        assertEquals("著名演员李四", insertedActor.getDescription());
    }

    @Test
    public void testUpdateById() {
        Actor actor = actorMapper.selectById(1L);
        assertNotNull(actor);

        actor.setName("张三丰");
        actor.setPhoto("/images/actors/zhangsan_new.jpg");
        actor.setDescription("著名演员张三丰");

        int result = actorMapper.updateById(actor);
        assertEquals(1, result);

        Actor updatedActor = actorMapper.selectById(1L);
        assertNotNull(updatedActor);
        assertEquals("张三丰", updatedActor.getName());
        assertEquals("/images/actors/zhangsan_new.jpg", updatedActor.getPhoto());
        assertEquals("著名演员张三丰", updatedActor.getDescription());
    }

    @Test
    public void testDeleteById() {
        int result = actorMapper.deleteById(1L);
        assertEquals(1, result);

        Actor deletedActor = actorMapper.selectById(1L);
        assertEquals(null, deletedActor);
    }

}
