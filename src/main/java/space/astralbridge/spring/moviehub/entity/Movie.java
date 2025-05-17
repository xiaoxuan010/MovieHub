package space.astralbridge.spring.moviehub.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import space.astralbridge.spring.moviehub.config.JsonViewConfig;

@Data
@TableName("movie")
public class Movie {
    @TableId(type = IdType.AUTO)
    @JsonView(JsonViewConfig.BaseView.class)
    private Long id;

    @NotNull
    @JsonView(JsonViewConfig.BaseView.class)
    private String title;

    @JsonView(JsonViewConfig.BaseView.class)
    private String description;

    @JsonView(JsonViewConfig.BaseView.class)
    private String releaseDate;

    @JsonView(JsonViewConfig.BaseView.class)
    private String duration; // 电影时长

    @Nullable
    @JsonView(JsonViewConfig.BaseView.class)
    private String coverImage;

    // 只在管理员视图中可见
    @JsonView(JsonViewConfig.AdminView.class)
    private String videoUrl;

    @JsonView(JsonViewConfig.BaseView.class)
    private String region;

    @NotNull
    @JsonView(JsonViewConfig.BaseView.class)
    private Integer isVip = 0; // 0-否，1-是

    @JsonView(JsonViewConfig.BaseView.class)
    private Integer playCount = 0;

    @JsonView(JsonViewConfig.BaseView.class)
    private Double score;

    @TableField(exist = false)
    @JsonView(JsonViewConfig.BaseView.class)
    private List<MovieType> movieTypes;

    @TableField(exist = false)
    @JsonView(JsonViewConfig.BaseView.class)
    private List<Director> directors;

    @TableField(exist = false)
    @JsonView(JsonViewConfig.BaseView.class)
    private List<Actor> actors;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @TableField(fill = FieldFill.INSERT)
    @JsonView(JsonViewConfig.BaseView.class)
    private LocalDateTime createTime;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonView(JsonViewConfig.BaseView.class)
    private LocalDateTime updateTime;
}
