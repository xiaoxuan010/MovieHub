package space.astralbridge.spring.moviehub.dto;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateMovieRequest {

    @NotNull
    private Long id;

    private String title;

    private String description;

    private LocalDate releaseDate;

    private String duration; // 电影时长

    private String coverImage;

    private String videoUrl; // 添加视频URL字段

    private String region;

    private Integer isVip; // 0-否，1-是

    private Integer playCount;

    private Double score;

    private List<Long> movieTypeIds;
    private List<Long> directorIds;
    private List<Long> actorIds;
}
