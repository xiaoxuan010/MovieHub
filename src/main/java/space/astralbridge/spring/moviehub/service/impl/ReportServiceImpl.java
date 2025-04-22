package space.astralbridge.spring.moviehub.service.impl;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import space.astralbridge.spring.moviehub.dto.MovieLeaderboardDTO;
import space.astralbridge.spring.moviehub.entity.Movie;
import space.astralbridge.spring.moviehub.entity.MovieType;
import space.astralbridge.spring.moviehub.mapper.MovieMapper;
import space.astralbridge.spring.moviehub.service.ReportService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private MovieMapper movieMapper;

    @Override
    public Workbook exportMoviesToExcel(String region, Long typeId) {
        // 1. 查询电影数据
        List<Movie> movies = movieMapper.findMoviesByCondition(region, typeId);
        
        // 2. 创建Excel工作簿
        Workbook workbook = new XSSFWorkbook(); // .xlsx格式
        Sheet sheet = workbook.createSheet("电影数据");
        
        // 3. 创建标题行样式
        CellStyle headerStyle = createHeaderStyle(workbook);
        
        // 4. 创建标题行
        Row headerRow = sheet.createRow(0);
        String[] headers = {"ID", "电影名称", "导演", "演员", "类型", "地区", "上映日期", "时长(分钟)", 
                           "是否VIP", "评分", "播放量"};
        
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        
        // 5. 填充数据行
        int rowNum = 1;
        for (Movie movie : movies) {
            Row row = sheet.createRow(rowNum++);
            
            row.createCell(0).setCellValue(movie.getId());
            row.createCell(1).setCellValue(movie.getTitle());
            
            // 处理导演信息 - 将多个导演名称用逗号拼接
            String directors = movie.getDirectors() != null ? 
                movie.getDirectors().stream()
                    .map(director -> director.getName())
                    .collect(Collectors.joining(", ")) : "";
            row.createCell(2).setCellValue(directors);
            
            // 处理演员信息 - 将多个演员名称用逗号拼接
            String actors = movie.getActors() != null ? 
                movie.getActors().stream()
                    .map(actor -> actor.getName())
                    .collect(Collectors.joining(", ")) : "";
            row.createCell(3).setCellValue(actors);
            
            // 处理类型信息 - 将多个类型名称用逗号拼接
            String types = movie.getMovieTypes() != null ? 
                movie.getMovieTypes().stream()
                    .map(MovieType::getName)
                    .collect(Collectors.joining(", ")) : "";
            row.createCell(4).setCellValue(types);
            
            row.createCell(5).setCellValue(movie.getRegion() != null ? movie.getRegion() : "");
            row.createCell(6).setCellValue(movie.getReleaseDate() != null ? movie.getReleaseDate().toString() : "");
            row.createCell(7).setCellValue(movie.getDuration() != null ? movie.getDuration() : "");
            row.createCell(8).setCellValue(movie.getIsVip() != null && movie.getIsVip() == 1 ? "是" : "否");
            row.createCell(9).setCellValue(movie.getScore() != null ? movie.getScore() : 0.0);
            row.createCell(10).setCellValue(movie.getPlayCount() != null ? movie.getPlayCount() : 0);
        }
        
        // 6. 自动调整列宽
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
        
        return workbook;
    }
    
    @Override
    public MovieLeaderboardDTO getPlayCountLeaderboard(Integer top) {
        // 默认返回前10部电影
        int limit = (top == null || top < 1) ? 10 : top;
        
        // 查询播放量最高的电影列表
        List<Movie> movies = movieMapper.selectTopNMoviesByPlayCount(limit);
        
        // 构建排行榜数据
        List<MovieLeaderboardDTO.MovieRankItem> rankItems = new ArrayList<>();
        
        // 填充排行榜数据
        for (int i = 0; i < movies.size(); i++) {
            Movie movie = movies.get(i);
            MovieLeaderboardDTO.MovieRankItem item = new MovieLeaderboardDTO.MovieRankItem(
                i + 1,                          // 排名，从1开始
                movie.getId(),                 // 电影ID
                movie.getTitle(),              // 电影标题
                movie.getPlayCount() != null ? movie.getPlayCount() : 0,  // 播放量
                movie.getScore() != null ? movie.getScore() : 0.0,        // 评分
                movie.getRegion() != null ? movie.getRegion() : "",       // 地区
                movie.getCoverImage() != null ? movie.getCoverImage() : "" // 封面图片
            );
            rankItems.add(item);
        }
        
        return new MovieLeaderboardDTO(rankItems);
    }
    
    /**
     * 创建标题行样式
     */
    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }
} 