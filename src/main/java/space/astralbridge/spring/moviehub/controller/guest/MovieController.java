package space.astralbridge.spring.moviehub.controller.guest;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.RequiredArgsConstructor;
import space.astralbridge.spring.moviehub.common.Result;
import space.astralbridge.spring.moviehub.common.ResultCode;
import space.astralbridge.spring.moviehub.config.JsonViewConfig;
import space.astralbridge.spring.moviehub.entity.Movie;
import space.astralbridge.spring.moviehub.service.MovieService;

@RestController
@RequestMapping("/api/guest/movies")
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;

    @GetMapping
    @JsonView(JsonViewConfig.GuestView.class)
    public Result<Page<Movie>> getAllMovies(@RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size) {
        Page<Movie> moviePage = new Page<>(current, size);
        return Result.success(movieService.page(moviePage));
    }

    @GetMapping("/{id}")
    @JsonView(JsonViewConfig.GuestView.class)
    public Result<Movie> getMovieById(@PathVariable Long id) {
        return Result.success(movieService.getById(id));
    }

    @GetMapping("/director/{directorId}")
    @JsonView(JsonViewConfig.GuestView.class)
    public Result<Page<Movie>> getMoviesByDirector(
            @PathVariable Long directorId,
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size) {
        Page<Movie> moviePage = new Page<>(current, size);
        return Result.success(movieService.getMoviesByDirector(directorId, moviePage));
    }

    @GetMapping("/actor/{actorId}")
    @JsonView(JsonViewConfig.GuestView.class)
    public Result<Page<Movie>> getMoviesByActor(
            @PathVariable Long actorId,
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size) {
        Page<Movie> moviePage = new Page<>(current, size);
        return Result.success(movieService.getMoviesByActor(actorId, moviePage));
    }

    /**
     * 获取电影资源URL
     * 
     * @param id 电影ID
     * @return 包含视频URL的结果
     */
    @GetMapping("/{id}/video-url")
    public Result<Map<String, String>> getMovieVideoUrl(@PathVariable Long id) {
        // 获取电影信息
        Movie movie = movieService.getById(id);
        if (movie == null) {
            return Result.fail("电影不存在");
        }

        // 验证用户权限（示例：实际应用中应该从安全上下文获取用户信息）
        boolean hasAccess = checkUserAccess(movie);
        if (!hasAccess) {
            return Result.fail(ResultCode.FORBIDDEN, "无权访问此资源，请升级为VIP会员");
        }

        Map<String, String> result = new HashMap<>();
        result.put("videoUrl", movie.getVideoUrl());
        
        movie.setPlayCount(movie.getPlayCount() + 1);
        movieService.updateById(movie);

        return Result.success(result);
    }

    /**
     * 检查用户是否有权限访问电影资源
     * 
     * @param movie 电影信息
     * @return 是否有权限
     */
    private boolean checkUserAccess(Movie movie) {
        // 如果电影不需要VIP权限，直接返回true
        if (movie.getIsVip() == 0) {
            return true;
        }

        // 获取当前认证信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 检查是否有ROLE_ADMIN或ROLE_VIP权限
        return authentication != null
                && (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")) ||
                        authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_VIP")));
    }
}
