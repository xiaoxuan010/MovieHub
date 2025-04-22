package space.astralbridge.spring.moviehub.dto;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateMovieRequest {
    @NotNull
    private String title;

    @NotNull
    private String description;

    private LocalDate releaseDate;

    /**
     * 电影时长
     */
    private String duration;

    private String coverImage;

    private String region;

    /**
     * 是否VIP
     * 0-否，1-是
     */
    @NotNull
    private Integer isVip;

    private Double score;

    private List<Long> movieTypeIds;
    private List<Long> directorIds;
    private List<ActorDto> actors;
}
