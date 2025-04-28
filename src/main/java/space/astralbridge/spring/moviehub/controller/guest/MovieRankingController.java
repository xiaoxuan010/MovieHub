package space.astralbridge.spring.moviehub.controller.guest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import space.astralbridge.spring.moviehub.common.Result;
import space.astralbridge.spring.moviehub.entity.Movie;
import space.astralbridge.spring.moviehub.service.MovieRankingService;

@RestController
@RequestMapping("/api/guest/movies/ranking") // 定义基础路径
@RequiredArgsConstructor // 使用 Lombok 自动生成构造函数注入
public class MovieRankingController {

    private final MovieRankingService movieRankingService; // 注入排行榜服务

    /**
     * 获取电影排行榜 API
     *
     * @param sortBy  排序字段 (可选, 默认 "score"). 支持 "score", "play_count", "release_date", "id".
     * @param order   排序顺序 (可选, 默认 "desc"). 支持 "asc", "desc".
     * @param current 当前页码 (可选, 默认 1).
     * @param size    每页大小 (可选, 默认 10).
     * @return 包含电影排行榜分页数据的 Result 对象
     */
    @GetMapping
    public Result<Page<Movie>> getMovieRanking(
            @RequestParam(required = false, defaultValue = "score") String sortBy,
            @RequestParam(required = false, defaultValue = "desc") String order,
            @RequestParam(required = false, defaultValue = "1") Integer current,
            @RequestParam(required = false, defaultValue = "10") Integer size) {

        // 调用 Service 层获取排行榜数据
        Page<Movie> rankedMoviesPage = movieRankingService.getRankedMovies(sortBy, order, current, size);

        // 使用 Result 工具类包装成功响应
        return Result.success(rankedMoviesPage);
    }
}
