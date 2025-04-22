package space.astralbridge.spring.moviehub.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import space.astralbridge.spring.moviehub.common.Result;
import space.astralbridge.spring.moviehub.dto.MoviePlayCountDTO;
import space.astralbridge.spring.moviehub.dto.MovieTypeCountDTO;
import space.astralbridge.spring.moviehub.service.StatsService;

@RestController
@RequestMapping("/api/admin/stats")
@PreAuthorize("hasRole('ADMIN')")
public class AdminStatsController {

    @Autowired
    private StatsService statsService;

    /**
     * 按类型统计电影数量
     * @return 包含所有电影类型名称列表及其对应的电影数量列表
     */
    @GetMapping("/movie-count-by-type")
    public Result<MovieTypeCountDTO> getMovieCountByType() {
        try {
            MovieTypeCountDTO data = statsService.getMovieCountByType();
            return Result.success(data);
        } catch (Exception e) {
            return Result.fail("获取电影类型统计数据失败: " + e.getMessage());
        }
    }

    /**
     * 获取播放量最高的N部电影统计
     * @param top 指定返回的电影数量，默认为5
     * @return 包含电影标题和播放量的数据
     */
    @GetMapping("/top-movies-by-playcount")
    public Result<MoviePlayCountDTO> getTopMoviesByPlayCount(
            @RequestParam(value = "top", required = false, defaultValue = "5") Integer top) {
        try {
            MoviePlayCountDTO data = statsService.getTopMoviesByPlayCount(top);
            return Result.success(data);
        } catch (Exception e) {
            return Result.fail("获取热门电影统计数据失败: " + e.getMessage());
        }
    }
}