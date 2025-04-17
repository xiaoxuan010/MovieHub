package space.astralbridge.spring.moviehub.mapper;

import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import space.astralbridge.spring.moviehub.entity.Movie;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@MybatisPlusTest
@Transactional
public class MovieMapperTest {
    @Autowired
    private MovieMapper movieMapper;

    public void assertMovie1(Movie movie) {
        System.out.println(movie);

        assertNotNull(movie);
        assertEquals("电影一", movie.getTitle());
        assertEquals("这是电影一的描述", movie.getDescription());
        assertEquals("2023-01-15", movie.getReleaseDate().toString());
        assertEquals(120, movie.getDuration());
        assertEquals("/images/movies/movie1.jpg", movie.getCoverImage());
        assertEquals("中国", movie.getRegion());
        assertEquals(0, movie.getIsVip());
        assertEquals(1000, movie.getPlayCount());
        assertEquals(8.5, movie.getScore());

        assertNotNull(movie.getMovieTypes());
        assertEquals(2, movie.getMovieTypes().size());
        assertEquals("动作", movie.getMovieTypes().get(0).getName());
        assertEquals("剧情", movie.getMovieTypes().get(1).getName());

        assertNotNull(movie.getCreateTime());
        assertNotNull(movie.getUpdateTime());
    }

    @Test
    public void testSelectById() {
        Movie found = movieMapper.selectById(1L);
        assertMovie1(found);
    }

    @Test
    public void testSelectList() {
        List<Movie> movies = movieMapper.selectList(null);
        assertNotNull(movies);
        assertNotNull(movies.getFirst());
        assertMovie1(movies.getFirst());
    }
}