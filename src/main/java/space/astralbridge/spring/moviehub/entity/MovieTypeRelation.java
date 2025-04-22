package space.astralbridge.spring.moviehub.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("movie_movie_type")
public class MovieTypeRelation {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long movieId;
    private Long typeId;

    public MovieTypeRelation(Long movieId, Long typeId) {
        this.movieId = movieId;
        this.typeId = typeId;
    }
}
