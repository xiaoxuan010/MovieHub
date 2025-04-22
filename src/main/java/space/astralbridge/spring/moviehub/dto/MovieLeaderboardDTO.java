package space.astralbridge.spring.moviehub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 电影播放量排行榜DTO
 * 用于返回按播放量排序的电影列表数据
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieLeaderboardDTO {
    private List<MovieRankItem> items;
    
    /**
     * 排行榜中的单个电影项
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MovieRankItem {
        private Integer rank;
        private Long id;
        private String title;
        private Integer playCount;
        private Double score;
        private String region;
        private String coverImage;
    }
} 