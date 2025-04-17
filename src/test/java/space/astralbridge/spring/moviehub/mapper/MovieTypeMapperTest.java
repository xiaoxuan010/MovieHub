package space.astralbridge.spring.moviehub.mapper;

import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import space.astralbridge.spring.moviehub.entity.MovieType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@MybatisPlusTest
@Transactional
public class MovieTypeMapperTest {
    @Autowired
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