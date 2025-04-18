package space.astralbridge.spring.moviehub.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateMovieRequest {
    @NotNull
    private Long id;

    private String title;

    private String description;

    private LocalDate releaseDate;

    private Integer duration; // 单位：分钟

    private String coverImage;

    private String region;

    private Integer isVip; // 0-否，1-是

    private Integer playCount;

    private Double score;
}
