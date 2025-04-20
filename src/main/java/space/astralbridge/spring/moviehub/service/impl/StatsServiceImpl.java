package space.astralbridge.spring.moviehub.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import space.astralbridge.spring.moviehub.dto.MoviePlayCountDTO;
import space.astralbridge.spring.moviehub.dto.MovieTypeCountDTO;
import space.astralbridge.spring.moviehub.mapper.MovieMapper;
import space.astralbridge.spring.moviehub.service.StatsService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class StatsServiceImpl implements StatsService {

    @Autowired
    private MovieMapper movieMapper;

    @Override
    public MovieTypeCountDTO getMovieCountByType() {
        List<Map<String, Object>> statsData = movieMapper.countMoviesByType();

        List<String> types = new ArrayList<>();
        List<Integer> counts = new ArrayList<>();

        if (statsData != null) {
            for (Map<String, Object> data : statsData) {
                // 尝试不同的键名
                String typeName = null;
                Number movieCount = null;

                // 尝试小写键名
                if (data.containsKey("type_name")) {
                    typeName = (String) data.get("type_name");
                    movieCount = (Number) data.get("movie_count");
                }
                // 尝试大写键名
                else if (data.containsKey("TYPE_NAME")) {
                    typeName = (String) data.get("TYPE_NAME");
                    movieCount = (Number) data.get("MOVIE_COUNT");
                }
                // 尝试驼峰命名
                else if (data.containsKey("typeName")) {
                    typeName = (String) data.get("typeName");
                    movieCount = (Number) data.get("movieCount");
                }

                if (typeName != null) {
                    types.add(typeName);
                    // 如果电影数量为null，则默认为0
                    counts.add(movieCount != null ? movieCount.intValue() : 0);
                }
            }
        }

        return new MovieTypeCountDTO(types, counts);
    }

    @Override
    public MoviePlayCountDTO getTopMoviesByPlayCount(Integer top) {
        // 如果top为null或小于1，使用默认值5
        int limit = (top == null || top < 1) ? 5 : top;

        // 查询播放量最高的N部电影
        List<Map<String, Object>> moviesData = movieMapper.selectTopMoviesByPlayCount(limit);

        List<String> titles = new ArrayList<>();
        List<Integer> playCounts = new ArrayList<>();

        if (moviesData != null) {
            for (Map<String, Object> data : moviesData) {
                // 尝试不同的键名
                String title = null;
                Number playCount = null;

                // 尝试小写键名
                if (data.containsKey("title")) {
                    title = (String) data.get("title");
                    playCount = (Number) data.get("play_count");
                }
                // 尝试大写键名
                else if (data.containsKey("TITLE")) {
                    title = (String) data.get("TITLE");
                    playCount = (Number) data.get("PLAY_COUNT");
                }
                // 尝试驼峰命名
                else if (data.containsKey("movieTitle")) {
                    title = (String) data.get("movieTitle");
                    playCount = (Number) data.get("playCount");
                }

                if (title != null) {
                    titles.add(title);
                    // 如果播放量为null，则默认为0
                    playCounts.add(playCount != null ? playCount.intValue() : 0);
                }
            }
        }

        return new MoviePlayCountDTO(titles, playCounts);
    }
}