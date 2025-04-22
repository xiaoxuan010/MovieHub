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

    public void assertDirector1(Director director) {
        assertNotNull(director);
        assertEquals("赵导", director.getName());
        assertEquals("/images/directors/zhao.jpg", director.getPhoto());
        assertEquals("著名导演赵导", director.getDescription());
    }

    @Test
    public void testSelectById() {
        Director found = directorMapper.selectById(1L);
        assertDirector1(found);
    }

    @Test
    public void testSelectList() {
        List<Director> directors = directorMapper.selectList(null);
        assertNotNull(directors);
        assertNotNull(directors.get(0));
        assertDirector1(directors.get(0));
    }

    @Test
    public void testInsert() {
        Director director = new Director();
        director.setName("郭帆");
        director.setPhoto("/images/directors/guofan.jpg");
        director.setDescription("著名导演郭帆，作品包括《流浪地球》和《流浪地球2》");

        int rows = directorMapper.insert(director);

        assertEquals(1, rows);
        assertNotNull(director.getId());
    }

    @Test
    public void testUpdateById() {
        Director director = directorMapper.selectById(1L);
        assertNotNull(director);
        director.setName("赵导 (Updated)");
        director.setPhoto("/images/directors/zhao_updated.jpg");
        director.setDescription("Updated description for 赵导");

        directorMapper.updateById(director);

        Director updatedDirector = directorMapper.selectById(1L);
        assertNotNull(updatedDirector);
        assertEquals("赵导 (Updated)", updatedDirector.getName());
        assertEquals("/images/directors/zhao_updated.jpg", updatedDirector.getPhoto());
        assertEquals("Updated description for 赵导", updatedDirector.getDescription());
    }

    @Test
    public void testDeleteById() {
        int rows = directorMapper.deleteById(1L);
        assertEquals(1, rows);

        Director deletedDirector = directorMapper.selectById(1L);
        assertNull(deletedDirector);
    }
}