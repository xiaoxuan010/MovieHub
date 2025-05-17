package space.astralbridge.spring.moviehub.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;

import space.astralbridge.spring.moviehub.entity.Movie;

@MybatisPlusTest
@Transactional
public class MovieMapperTest {
    @Autowired
    private MovieMapper movieMapper;

    @Test
    void testBasicCRUD() {
        // Create
        Movie movie = new Movie();
        movie.setTitle("Test Movie");
        movie.setDescription("Test Description");
        movie.setPlayCount(100);
        movie.setRegion("US");
        movie.setCreateTime(LocalDateTime.now());
        movie.setUpdateTime(LocalDateTime.now());

        int insert = movieMapper.insert(movie);
        assertEquals(1, insert);
        assertNotNull(movie.getId());

        // Read
        Movie found = movieMapper.selectById(movie.getId());
        assertNotNull(found);
        assertEquals("Test Movie", found.getTitle());
        assertEquals("Test Description", found.getDescription());

        // Update
        found.setTitle("Updated Movie");
        found.setPlayCount(200);
        int update = movieMapper.updateById(found);
        assertEquals(1, update);

        Movie updated = movieMapper.selectById(found.getId());
        assertEquals("Updated Movie", updated.getTitle());
        assertEquals(200, updated.getPlayCount());

        // Delete
        int delete = movieMapper.deleteById(updated.getId());
        assertEquals(1, delete);

        Movie deleted = movieMapper.selectById(updated.getId());
        assertNull(deleted);
    }
}