package space.astralbridge.spring.moviehub.dto;

import lombok.Data;

@Data
public class MovieExcelData {
    private String title;

    private String description;

    private String releaseDate;

    private String duration;

    private String coverImage;

    private String region;

    private Integer isVip;

    private String movieTypes;

    private String directors;

    private String actors;

    private Double score;
}