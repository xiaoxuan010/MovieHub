package space.astralbridge.spring.moviehub.service;

import space.astralbridge.spring.moviehub.dto.MoviePlayCountDTO;
import space.astralbridge.spring.moviehub.dto.MovieTypeCountDTO;

public interface StatsService {
    /**
     * 按类型统计电影数量
     * @return 电影类型和数量统计数据
     */
    MovieTypeCountDTO getMovieCountByType();

    /**
     * 获取播放量最高的N部电影
     * @param top 返回的电影数量，默认为5
     * @return 电影标题和播放量统计数据
     */
    MoviePlayCountDTO getTopMoviesByPlayCount(Integer top);
}