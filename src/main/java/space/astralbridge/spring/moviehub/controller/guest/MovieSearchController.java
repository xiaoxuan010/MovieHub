package space.astralbridge.spring.moviehub.controller.guest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import space.astralbridge.spring.moviehub.common.Result;
import space.astralbridge.spring.moviehub.entity.Movie;
import space.astralbridge.spring.moviehub.service.MovieService;

/**
 * 电影搜索控制器 (访客权限)
 * 提供电影搜索相关的 API 接口。
 */
@RestController
@RequestMapping("/api/guest/movies") // 基础路径保持与其他电影相关 guest API 一致
@RequiredArgsConstructor // 自动注入 final 字段
public class MovieSearchController {

    private final MovieService movieService; // 注入电影服务

    /**
     * 搜索电影 API
     * 根据提供的查询关键词搜索电影（标题、演员、导演）。
     *
     * @param query   搜索关键词 (必需)
     * @param current 当前页码 (可选, 默认为 1)
     * @param size    每页显示数量 (可选, 默认为 10)
     * @return 包含分页电影搜索结果的 Result 对象
     */
    @GetMapping("/search") // 定义搜索端点的具体路径
    public Result<Page<Movie>> searchMovies(
            @RequestParam(required = true) String query, // 'query' 参数是必需的
            @RequestParam(required = false, defaultValue = "1") Integer current, // 'current' 页码，默认为 1
            @RequestParam(required = false, defaultValue = "10") Integer size) { // 'size' 每页数量，默认为 10

        // 创建 MybatisPlus 的分页对象
        Page<Movie> page = new Page<>(current, size);

        // 调用 Service 层执行搜索，并传入分页对象和查询关键词
        Page<Movie> searchResultPage = movieService.searchMovies(query, page);

        // 使用 Result 工具类包装成功的响应，返回分页结果
        return Result.success(searchResultPage);
    }
}