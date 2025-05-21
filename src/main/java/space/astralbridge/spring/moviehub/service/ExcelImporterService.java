package space.astralbridge.spring.moviehub.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import lombok.RequiredArgsConstructor;
import space.astralbridge.spring.moviehub.common.utils.RedisTemplateUtils;
import space.astralbridge.spring.moviehub.dto.MovieExcelData;
import space.astralbridge.spring.moviehub.entity.Movie;
import space.astralbridge.spring.moviehub.entity.MovieActorRelation;
import space.astralbridge.spring.moviehub.entity.MovieDirectorRelation;
import space.astralbridge.spring.moviehub.entity.MovieTypeRelation;
import space.astralbridge.spring.moviehub.mapper.MovieActorRelationMapper;
import space.astralbridge.spring.moviehub.mapper.MovieDirectorRelationMapper;
import space.astralbridge.spring.moviehub.mapper.MovieMapper;
import space.astralbridge.spring.moviehub.mapper.MovieTypeRelationMapper;

@Service
@RequiredArgsConstructor
public class ExcelImporterService {

    private final RedisTemplateUtils redisTemplateUtils;
    private final ModelMapper modelMapper;
    private final MovieMapper movieMapper;
    private final MovieTypeRelationMapper movieTypeRelationMapper;
    private final MovieDirectorRelationMapper movieDirectorRelationMapper;
    private final MovieActorRelationMapper movieActorRelationMapper;

    private static String getCellValue(Cell cell) {
        if (cell == null)
            return null;
        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue();
        } else if (cell.getCellType() == CellType.NUMERIC) {
            return String.valueOf(cell.getNumericCellValue());
        }
        return null;
    }

    private List<Movie> importMovies(Workbook workbook) throws IOException {
        StopWatch stopWatch = new StopWatch();
        List<Movie> movies;
        try (workbook) {
            stopWatch.start("Excel 数据解析");
            Sheet sheet = workbook.getSheetAt(0);
            List<MovieExcelData> excelMovies = new ArrayList<>();
            for (int i = 1; i <= sheet.getLastRowNum(); i++) { // 跳过表头
                Row row = sheet.getRow(i);
                if (row == null)
                    continue;

                MovieExcelData excelMovie = new MovieExcelData();
                excelMovie.setCoverImage(getCellValue(row.getCell(0)));
                excelMovie.setTitle(getCellValue(row.getCell(1)));
                excelMovie.setDescription(getCellValue(row.getCell(2)));
                excelMovie.setDirectors(getCellValue(row.getCell(3)));
                excelMovie.setActors(getCellValue(row.getCell(4)));
                excelMovie.setMovieTypes(getCellValue(row.getCell(5)));
                excelMovie.setReleaseDate(getCellValue(row.getCell(6)));
                excelMovie.setRegion(getCellValue(row.getCell(7)));
                excelMovie.setDuration(getCellValue(row.getCell(8)));
                excelMovie.setIsVip(Double.valueOf(getCellValue(row.getCell(9))).intValue());
                excelMovie.setScore(Double.valueOf(getCellValue(row.getCell(10))));

                excelMovies.add(excelMovie);
            }
            stopWatch.stop();

            stopWatch.start("电影数据转换");
            // 将 Excel 数据转换为 Movie 实体
            movies = modelMapper.map(excelMovies, new TypeToken<List<Movie>>() {
            }.getType());
            stopWatch.stop();

            stopWatch.start("电影数据插入");
            movieMapper.insert(movies);
            stopWatch.stop();

            List<MovieTypeRelation> movieTypeRelations = new ArrayList<>();
            List<MovieDirectorRelation> movieDirectorRelations = new ArrayList<>();
            List<MovieActorRelation> movieActorRelations = new ArrayList<>();
            stopWatch.start("关系数据生成");
            movies.forEach(movie -> {
                // 处理电影类型
                if (movie.getMovieTypes() != null) {
                    movie.getMovieTypes().forEach(movieType -> movieTypeRelations
                            .add(new MovieTypeRelation(movie.getId(), movieType.getId())));
                }

                // 处理导演关系
                if (movie.getDirectors() != null) {
                    movie.getDirectors().forEach(director -> movieDirectorRelations
                            .add(new MovieDirectorRelation(movie.getId(), director.getId())));
                }

                // 处理演员关系
                if (movie.getActors() != null) {
                    movie.getActors().forEach(
                            actor -> movieActorRelations.add(new MovieActorRelation(movie.getId(), actor.getId())));
                }
            });
            stopWatch.stop();
            stopWatch.start("关系数据插入");
            movieTypeRelationMapper.insert(movieTypeRelations);
            movieDirectorRelationMapper.insert(movieDirectorRelations);
            movieActorRelationMapper.insert(movieActorRelations);
            stopWatch.stop();
        }

        System.out.println(stopWatch.prettyPrint());
        return movies;
    }

    public List<Movie> importMovies(InputStream is) throws IOException {
        redisTemplateUtils.evictCacheByPrefix("movies:");

        return importMovies(new XSSFWorkbook(is));
    }
}