package space.astralbridge.spring.moviehub.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("movie_actor")
public class MovieActorRelation {
    private Long id;
    private Long movieId;
    private Long actorId;
    private String role;

    public MovieActorRelation(Long movieId, Long actorId, String role) {
        this.movieId = movieId;
        this.actorId = actorId;
        this.role = role;
    }
}
