package space.astralbridge.spring.moviehub.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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

        assertNotNull(movie.getDirectors());
        assertEquals(1, movie.getDirectors().size());
        assertEquals("赵导", movie.getDirectors().getFirst().getName());
        assertEquals("/images/directors/zhao.jpg", movie.getDirectors().getFirst().getPhoto());
        assertEquals("著名导演赵导", movie.getDirectors().getFirst().getDescription());

        assertNotNull(movie.getActors());
        assertEquals(2, movie.getActors().size());
        assertEquals("张三", movie.getActors().getFirst().getName());
        assertEquals("主角A", movie.getActors().getFirst().getRole());
        assertEquals("/images/actors/zhangsan.jpg", movie.getActors().get(0).getPhoto());
        assertEquals("著名演员张三", movie.getActors().get(0).getDescription());
        assertEquals("李四", movie.getActors().get(1).getName());
        assertEquals("配角B", movie.getActors().get(1).getRole());
        assertEquals("/images/actors/lisi.jpg", movie.getActors().get(1).getPhoto());
        assertEquals("著名演员李四", movie.getActors().get(1).getDescription());

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

    @Test
    public void testInsert() {
        Movie movie = new Movie();
        movie.setTitle("电影5");
        movie.setDescription("这是电影五的描述");
        movie.setReleaseDate(LocalDate.parse("2025-04-18"));
        movie.setDuration(150);
        movie.setCoverImage("/images/movies/movie5.jpg");
        movie.setRegion("美国");
        movie.setIsVip(1);
        movie.setPlayCount(2000);
        movie.setScore(9.0);

        int rows = movieMapper.insert(movie);

        assertEquals(1, rows);
        assertNotNull(movie.getId());
    }

    @Test
    public void testUpdateById() throws InterruptedException {
        Thread.sleep(1);
        Movie movie = movieMapper.selectById(1L);
        assertNotNull(movie);
        movie.setTitle("电影一（更新）");
        movie.setDescription("这是电影一的描述（更新）");
        movie.setReleaseDate(LocalDate.parse("2023-01-16"));
        movie.setDuration(130);
        movie.setCoverImage("/images/movies/movie1_updated.jpg");
        movie.setRegion("中国（更新）");
        movie.setIsVip(1);
        movie.setPlayCount(1500);
        movie.setScore(9.0);
        movie.setUpdateTime(LocalDateTime.of(2025, 4, 18, 0, 0, 0));

        movieMapper.updateById(movie);

        Movie updatedMovie = movieMapper.selectById(1L);
        assertNotNull(updatedMovie);
        assertEquals("电影一（更新）", updatedMovie.getTitle());
        assertEquals("这是电影一的描述（更新）", updatedMovie.getDescription());
        assertEquals("2023-01-16", updatedMovie.getReleaseDate().toString());
        assertEquals(130, updatedMovie.getDuration());
        assertEquals("/images/movies/movie1_updated.jpg", updatedMovie.getCoverImage());
        assertEquals("中国（更新）", updatedMovie.getRegion());
        assertEquals(1, updatedMovie.getIsVip());
        assertEquals(1500, updatedMovie.getPlayCount());
        assertEquals(9.0, updatedMovie.getScore());

        assertEquals(LocalDateTime.of(2025, 4, 18, 0, 0, 0), updatedMovie.getUpdateTime());

    }
}