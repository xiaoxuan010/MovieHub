package space.astralbridge.spring.moviehub.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("movie_director")
public class MovieDirectorRelation {
    private Long id;
    private Long movieId;
    private Long directorId;

    public MovieDirectorRelation(Long movieId, Long directorId) {
        this.movieId = movieId;
        this.directorId = directorId;
    }
}
