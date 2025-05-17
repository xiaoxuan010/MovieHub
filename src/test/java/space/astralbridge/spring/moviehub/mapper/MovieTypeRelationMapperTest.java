package space.astralbridge.spring.moviehub.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;

import space.astralbridge.spring.moviehub.entity.MovieTypeRelation;

@MybatisPlusTest
class MovieTypeRelationMapperTest {

    @Autowired
    private MovieTypeRelationMapper movieTypeRelationMapper;

    @Test
    void testInsertAndRetrieveMovieTypeRelation() {
        // Arrange
        MovieTypeRelation relation = new MovieTypeRelation(1L, 2L);

        // Act
        int rowsInserted = movieTypeRelationMapper.insert(relation);
        MovieTypeRelation retrievedRelation = movieTypeRelationMapper.selectById(relation.getId());

        // Assert
        assertThat(rowsInserted).isEqualTo(1);
        assertThat(retrievedRelation).isNotNull();
        assertThat(retrievedRelation.getMovieId()).isEqualTo(1L);
        assertThat(retrievedRelation.getTypeId()).isEqualTo(2L);
    }

    @Test
    void testDeleteMovieTypeRelation() {
        // Arrange
        MovieTypeRelation relation = new MovieTypeRelation(1L, 2L);
        movieTypeRelationMapper.insert(relation);

        // Act
        int rowsDeleted = movieTypeRelationMapper.deleteById(relation.getId());

        // Assert
        assertThat(rowsDeleted).isEqualTo(1);
        MovieTypeRelation retrievedRelation = movieTypeRelationMapper.selectById(relation.getId());
        assertThat(retrievedRelation).isNull();
    }

    @Test
    void testUpdateMovieTypeRelation() {
        // Arrange
        MovieTypeRelation relation = new MovieTypeRelation(1L, 2L);
        movieTypeRelationMapper.insert(relation);

        // Act
        relation.setTypeId(3L);
        int rowsUpdated = movieTypeRelationMapper.updateById(relation);
        MovieTypeRelation retrievedRelation = movieTypeRelationMapper.selectById(relation.getId());

        // Assert
        assertThat(rowsUpdated).isEqualTo(1);
        assertThat(retrievedRelation).isNotNull();
        assertThat(retrievedRelation.getTypeId()).isEqualTo(3L);
    }

    @Test
    void testInsertAndRetrieveMovieTypeRelationList() {
        // Arrange
        MovieTypeRelation relation1 = new MovieTypeRelation(1L, 3L);
        MovieTypeRelation relation2 = new MovieTypeRelation(2L, 1L);
        List<MovieTypeRelation> relations = Arrays.asList(relation1, relation2);

        // Act
        movieTypeRelationMapper.insert(relations);

        MovieTypeRelation retrievedRelation1 = movieTypeRelationMapper.selectById(relation1.getId());
        MovieTypeRelation retrievedRelation2 = movieTypeRelationMapper.selectById(relation2.getId());

        // Assert
        assertThat(retrievedRelation1).isNotNull();
        assertThat(retrievedRelation1.getMovieId()).isEqualTo(1L);
        assertThat(retrievedRelation1.getTypeId()).isEqualTo(3L);

        assertThat(retrievedRelation2).isNotNull();
        assertThat(retrievedRelation2.getMovieId()).isEqualTo(2L);
        assertThat(retrievedRelation2.getTypeId()).isEqualTo(1L);
    }
}