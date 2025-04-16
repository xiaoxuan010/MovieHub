package space.astralbridge.spring.moviehub.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import space.astralbridge.spring.moviehub.entity.MovieType;

@SpringBootTest
@Transactional
public class MovieTypeMapperTest {
    @Resource
    private MovieTypeMapper movieTypeMapper;

    @Test
    public void testSelectTypesByMovieId() {
        List<MovieType> movieTypes = movieTypeMapper.selectTypesByMovieId(1L);
        System.out.println(movieTypes);

        assertNotNull(movieTypes);
        assertEquals(2, movieTypes.size());
        assertEquals("动作", movieTypes.get(0).getName());
        assertEquals("剧情", movieTypes.get(1).getName());

    }
}