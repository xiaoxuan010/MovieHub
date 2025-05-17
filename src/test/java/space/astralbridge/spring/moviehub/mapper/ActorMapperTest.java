package space.astralbridge.spring.moviehub.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;

import space.astralbridge.spring.moviehub.entity.Actor;

@MybatisPlusTest
class ActorMapperTest {

    @Autowired
    private ActorMapper actorMapper;

    @Test
    void testInsertAndRetrieveActor() {
        Actor actor = new Actor();
        actor.setName("John Doe");
        actor.setPhoto("photo_url");
        actor.setDescription("An amazing actor");

        int insertResult = actorMapper.insert(actor);
        assertThat(insertResult).isEqualTo(1);
        assertThat(actor.getId()).isNotNull();

        Actor retrievedActor = actorMapper.selectById(actor.getId());
        assertThat(retrievedActor).isNotNull();
        assertThat(retrievedActor.getName()).isEqualTo("John Doe");
        assertThat(retrievedActor.getPhoto()).isEqualTo("photo_url");
        assertThat(retrievedActor.getDescription()).isEqualTo("An amazing actor");
    }

    @Test
    void testUpdateActor() {
        Actor actor = new Actor();
        actor.setName("Jane Doe");
        actor.setPhoto("photo_url");
        actor.setDescription("A talented actor");

        actorMapper.insert(actor);
        actor.setName("Jane Smith");
        actor.setDescription("An exceptional actor");

        int updateResult = actorMapper.updateById(actor);
        assertThat(updateResult).isEqualTo(1);

        Actor updatedActor = actorMapper.selectById(actor.getId());
        assertThat(updatedActor.getName()).isEqualTo("Jane Smith");
        assertThat(updatedActor.getDescription()).isEqualTo("An exceptional actor");
    }

    @Test
    void testDeleteActor() {
        Actor actor = new Actor();
        actor.setName("Mark Twain");
        actor.setPhoto("photo_url");
        actor.setDescription("A legendary actor");

        actorMapper.insert(actor);
        Long actorId = actor.getId();

        int deleteResult = actorMapper.deleteById(actorId);
        assertThat(deleteResult).isEqualTo(1);

        Actor deletedActor = actorMapper.selectById(actorId);
        assertThat(deletedActor).isNull();
    }
}