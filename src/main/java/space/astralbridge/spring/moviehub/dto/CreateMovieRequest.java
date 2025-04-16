package space.astralbridge.spring.moviehub.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateMovieRequest {
    @NotNull
    private String title;

    @NotNull
    private String description;

    private LocalDate releaseDate;

    /**
     * 电影时长
     * 单位：分钟
     */
    private Integer duration;

    private String coverImage;

    private String region;

    /**
     * 是否VIP
     * 0-否，1-是
     */
    @NotNull
    private Integer isVip;

    private Double score;
}
