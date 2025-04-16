package space.astralbridge.spring.moviehub.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import space.astralbridge.spring.moviehub.entity.Movie;

@SpringBootTest
@Transactional
public class MovieMapperTest {
    @Resource
    private MovieMapper movieMapper;

    @Test
    public void testSelectById() {
        Movie found = movieMapper.selectById(1L);
        System.out.println(found);

        assertNotNull(found);
        assertEquals("电影一", found.getTitle());
        assertEquals("这是电影一的描述", found.getDescription());
        assertEquals("2023-01-15", found.getReleaseDate().toString());
        assertEquals(120, found.getDuration());
        assertEquals("/images/movies/movie1.jpg", found.getCoverImage());
        assertEquals("中国", found.getRegion());
        assertEquals(0, found.getIsVip());
        assertEquals(1000, found.getPlayCount());
        assertEquals(8.5, found.getScore());

        assertNotNull(found.getMovieTypes());
        assertEquals(2, found.getMovieTypes().size());
        assertEquals("动作", found.getMovieTypes().get(0).getName());
        assertEquals("剧情", found.getMovieTypes().get(1).getName());

        assertNotNull(found.getCreateTime());
        assertNotNull(found.getUpdateTime());

    }
}