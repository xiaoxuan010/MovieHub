package space.astralbridge.spring.moviehub.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("movie_actor")
public class MovieActorRelation {
    private Long id;
    private Long movieId;
    private Long actorId;

    public MovieActorRelation(Long movieId, Long actorId) {
        this.movieId = movieId;
        this.actorId = actorId;
    }
}
