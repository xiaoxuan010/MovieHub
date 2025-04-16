package space.astralbridge.spring.moviehub.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("movie_type")
public class MovieType {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
}
