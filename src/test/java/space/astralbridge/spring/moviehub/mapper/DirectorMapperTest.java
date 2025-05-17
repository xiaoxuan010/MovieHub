package space.astralbridge.spring.moviehub.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;

import space.astralbridge.spring.moviehub.entity.Director;

@MybatisPlusTest
@Transactional
public class DirectorMapperTest {
    @Autowired
    private DirectorMapper directorMapper;

    @Test
    public void testInsertDirector() {
        Director director = new Director();
        director.setName("Christopher Nolan");
        int result = directorMapper.insert(director);
        assertEquals(1, result);
        assertNotNull(director.getId());
    }

    @Test
    public void testSelectDirectorById() {
        Director director = new Director();
        director.setName("Steven Spielberg");
        directorMapper.insert(director);

        Director fetchedDirector = directorMapper.selectById(director.getId());
        assertNotNull(fetchedDirector);
        assertEquals("Steven Spielberg", fetchedDirector.getName());
    }

    @Test
    public void testUpdateDirector() {
        Director director = new Director();
        director.setName("James Cameron");
        directorMapper.insert(director);

        director.setName("James Francis Cameron");
        int result = directorMapper.updateById(director);
        assertEquals(1, result);

        Director updatedDirector = directorMapper.selectById(director.getId());
        assertEquals("James Francis Cameron", updatedDirector.getName());
    }

    @Test
    public void testDeleteDirector() {
        Director director = new Director();
        director.setName("Quentin Tarantino");
        directorMapper.insert(director);

        int result = directorMapper.deleteById(director.getId());
        assertEquals(1, result);

        Director deletedDirector = directorMapper.selectById(director.getId());
        assertNull(deletedDirector);
    }

    @Test
    public void testSelectAllDirectors() {
        Director director1 = new Director();
        director1.setName("Martin Scorsese");
        directorMapper.insert(director1);

        Director director2 = new Director();
        director2.setName("Stanley Kubrick");
        directorMapper.insert(director2);

        List<Director> directors = directorMapper.selectList(null);
        assertNotNull(directors);
        assertEquals(4, directors.size());
    }
}
