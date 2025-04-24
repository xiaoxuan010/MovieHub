package space.astralbridge.spring.moviehub.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import space.astralbridge.spring.moviehub.common.Result;
import space.astralbridge.spring.moviehub.common.ResultCode;
import space.astralbridge.spring.moviehub.dto.CreateMovieRequest;
import space.astralbridge.spring.moviehub.dto.UpdateMovieRequest;
import space.astralbridge.spring.moviehub.entity.Movie;
import space.astralbridge.spring.moviehub.entity.MovieActorRelation;
import space.astralbridge.spring.moviehub.entity.MovieDirectorRelation;
import space.astralbridge.spring.moviehub.entity.MovieTypeRelation;
import space.astralbridge.spring.moviehub.service.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/admin/movies")
@RequiredArgsConstructor
public class AdminMovieController {
    private final ModelMapper modelMapper;
    private final MovieService movieService;
    private final MovieTypeRelationService movieTypeRelationService;
    private final MovieDirectorRelationService movieDirectorRelationService;
    private final MovieActorRelationService movieActorRelationService;
    private final ExcelImporterService excelImporterService;

    @GetMapping
    public Result<List<Movie>> getAllMovies() {
        return Result.success(movieService.list());
    }

    @GetMapping("/{id}")
    public Result<Movie> getMovieById(@PathVariable Long id) {
        Movie movie = movieService.getById(id);
        if (movie == null) {
            return Result.fail(ResultCode.VALIDATE_FAILED, "电影不存在");
        }
        return Result.success(movie);
    }

    @PostMapping
    @Transactional
    public Result<Movie> createMovie(@RequestBody CreateMovieRequest createMovieRequest) {
        Movie movie = modelMapper.map(createMovieRequest, Movie.class);
        // 保存基础电影信息
        movieService.save(movie);

        // 如果电影类型不为空，则保存电影类型关系
        if (createMovieRequest.getMovieTypeIds() != null) {
            movieTypeRelationService.saveBatch(createMovieRequest.getMovieTypeIds().stream()
                    .map(typeId -> new MovieTypeRelation(movie.getId(), typeId)).toList());
        }

        // 如果导演不为空，则保存电影导演关系
        if (createMovieRequest.getDirectorIds() != null) {
            movieDirectorRelationService.saveBatch(createMovieRequest.getDirectorIds().stream()
                    .map(directorId -> new MovieDirectorRelation(movie.getId(), directorId)).toList());

        }

        // 如果演员不为空，则保存电影演员关系
        if (createMovieRequest.getActors() != null) {
            movieActorRelationService.saveBatch(createMovieRequest.getActors().stream()
                    .map(actor -> new MovieActorRelation(movie.getId(), actor.getId())).toList());
        }
        return Result.success(movieService.getById(movie.getId()));
    }

    @PutMapping("/")
    @Transactional
    public Result<Movie> updateMovie(@RequestBody UpdateMovieRequest updateMovieRequest) {
        Movie movie = modelMapper.map(updateMovieRequest, Movie.class);
        movieService.updateById(movie);

        // 更新电影类型关系
        QueryWrapper<MovieTypeRelation> movieTypeQueryWrapper = new QueryWrapper<>();
        movieTypeQueryWrapper.eq("movie_id", movie.getId());
        if (updateMovieRequest.getMovieTypeIds() != null) {
            movieTypeRelationService.remove(movieTypeQueryWrapper);
            movieTypeRelationService.saveBatch(updateMovieRequest.getMovieTypeIds().stream()
                    .map(typeId -> new MovieTypeRelation(movie.getId(), typeId)).toList());
        }

        // 更新电影导演关系
        QueryWrapper<MovieDirectorRelation> movieDirectorQueryWrapper = new QueryWrapper<>();
        movieDirectorQueryWrapper.eq("movie_id", movie.getId());
        if (updateMovieRequest.getDirectorIds() != null) {
            movieDirectorRelationService.remove(movieDirectorQueryWrapper);
            movieDirectorRelationService.saveBatch(updateMovieRequest.getDirectorIds().stream()
                    .map(directorId -> new MovieDirectorRelation(movie.getId(), directorId)).toList());
        }

        // 更新电影演员关系
        QueryWrapper<MovieActorRelation> movieActorQueryWrapper = new QueryWrapper<>();
        movieActorQueryWrapper.eq("movie_id", movie.getId());
        if (updateMovieRequest.getActors() != null) {
            movieActorRelationService.remove(movieActorQueryWrapper);
            movieActorRelationService.saveBatch(updateMovieRequest.getActors().stream()
                    .map(actor -> new MovieActorRelation(movie.getId(), actor.getId())).toList());
        }

        return Result.success(movieService.getById(movie.getId()));
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteMovie(@PathVariable("id") Long id) {
        if (movieService.removeById(id)) {
            return Result.success(null);
        }
        return Result.fail(ResultCode.VALIDATE_FAILED, "删除电影失败");
    }

    @PostMapping("/import")
    public Result<List<Movie>> importMovies(@RequestParam MultipartFile file)
            throws IllegalStateException, IOException {
        return Result.success(excelImporterService.importMovies(file.getInputStream()));
    }
}
