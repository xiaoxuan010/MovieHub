package space.astralbridge.spring.moviehub.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@TableName("movie")
public class Movie {
    @TableId(type = IdType.AUTO)
    private Long id;

    @NotNull
    private String title;

    private String description;

    private String releaseDate;

    private String duration; // 电影时长

    @Nullable
    private String coverImage;

    private String region;

    @NotNull
    private Integer isVip = 0; // 0-否，1-是

    private Integer playCount = 0;

    private Double score;

    @TableField(exist = false)
    private List<MovieType> movieTypes;
    @TableField(exist = false)
    private List<Director> directors;
    @TableField(exist = false)
    private List<Actor> actors;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
