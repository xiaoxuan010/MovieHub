package space.astralbridge.spring.moviehub.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;

import space.astralbridge.spring.moviehub.entity.MovieType;

@MybatisPlusTest
class MovieTypeMapperTest {

    @Autowired
    private MovieTypeMapper movieTypeMapper;

    @Test
    void testInsertAndRetrieveMovieType() {
        MovieType movieType = new MovieType();
        movieType.setName("Action");

        movieTypeMapper.insert(movieType);
        MovieType retrievedMovieType = movieTypeMapper.selectById(movieType.getId());

        assertNotNull(retrievedMovieType);
        assertEquals("Action", retrievedMovieType.getName());
    }

    @Test
    void testDeleteMovieType() {
        MovieType movieType = new MovieType();
        movieType.setName("Comedy");
        movieTypeMapper.insert(movieType);

        movieTypeMapper.deleteById(movieType.getId());
        MovieType retrievedMovieType = movieTypeMapper.selectById(movieType.getId());

        assertNull(retrievedMovieType);
    }
}