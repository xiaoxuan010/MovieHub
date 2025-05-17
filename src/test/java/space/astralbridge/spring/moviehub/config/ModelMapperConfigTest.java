package space.astralbridge.spring.moviehub.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import space.astralbridge.spring.moviehub.dto.MovieExcelData;
import space.astralbridge.spring.moviehub.entity.Movie;
import space.astralbridge.spring.moviehub.entity.MovieType;

@SpringBootTest
class ModelMapperConfigTest {

    @Autowired
    private ModelMapper modelMapper;

    @Test
    void testModelMapperBeanNotNull() {
        assertNotNull(modelMapper, "ModelMapper bean should not be null");
    }

    @Test
    void testStringToMovieTypeListMapping() {
        // Arrange
        MovieExcelData movieExcelData = new MovieExcelData();
        movieExcelData.setMovieTypes("爱情 / 戏曲");

        // Act
        Movie movie = modelMapper.map(movieExcelData, Movie.class);

        // Assert
        assertNotNull(movie.getMovieTypes(), "Movie types should not be null");
        assertEquals(2, movie.getMovieTypes().size(), "Movie types size should be 2");

        List<String> movieTypeNames = movie.getMovieTypes().stream()
                .map(MovieType::getName)
                .toList();

        assertEquals(List.of("爱情", "戏曲"), movieTypeNames,
                "Movie type names should match expected values");
        assertEquals(5, movie.getMovieTypes().getFirst().getId());
        assertEquals(6, movie.getMovieTypes().get(1).getId());
    }
}