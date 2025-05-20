package space.astralbridge.spring.moviehub.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import space.astralbridge.spring.moviehub.entity.MovieType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class MovieTypeServiceIntegrationTest {

    @Autowired
    private MovieTypeService movieTypeService;

    @Test
    void testGetOrCreateByName_ExistingType() {
        // Arrange
        String typeName = "爱情";

        // 确保数据库中已存在该类型
        MovieType existingMovieType = movieTypeService.getOrCreateByName(typeName);

        // Act
        MovieType result = movieTypeService.getOrCreateByName(typeName);

        // Assert
        assertEquals(existingMovieType.getId(), result.getId());
        assertEquals(typeName, result.getName());
    }

    @Test
    void testGetOrCreateByName_NewType() {
        // Arrange
        String typeName = "戏曲";

        // Act
        MovieType result = movieTypeService.getOrCreateByName(typeName);

        // Assert
        assertEquals(typeName, result.getName());
        // 检查是否分配了一个新的ID
        assertTrue(result.getId() > 0);
    }
}
