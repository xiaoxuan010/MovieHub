package space.astralbridge.spring.moviehub.controller.admin;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import space.astralbridge.spring.moviehub.common.Result;
import space.astralbridge.spring.moviehub.dto.MovieLeaderboardDTO;
import space.astralbridge.spring.moviehub.entity.Movie;
import space.astralbridge.spring.moviehub.entity.MovieType;
import space.astralbridge.spring.moviehub.mapper.MovieMapper;
import space.astralbridge.spring.moviehub.service.ReportService;
import space.astralbridge.spring.moviehub.service.impl.ReportServiceImpl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 电影数据Excel导出功能测试类
 */
@SpringBootTest
@Transactional
public class AdminReportControllerTest {

    @Autowired
    private AdminReportController adminReportController;

    @Autowired
    private ReportService reportService;

    @Autowired
    private MovieMapper movieMapper;

    /**
     * 集成测试：测试导出所有电影Excel功能
     * 使用实际数据库数据进行完整流程测试
     */
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testExportAllMoviesExcel() throws IOException {
        // 1. 调用控制器方法（不带筛选条件，导出所有电影）
        ResponseEntity<byte[]> response = adminReportController.exportMoviesExcel(null, null);

        // 2. 验证响应状态码
        assertEquals(HttpStatus.OK, response.getStatusCode(), "响应状态码应为200 OK");

        // 3. 验证响应头
        assertNotNull(response.getHeaders().getContentType(), "Content-Type 不应为空");
        assertTrue(response.getHeaders().getContentType().toString()
                .contains("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
                "Content-Type 应为Excel格式");

        assertNotNull(response.getHeaders().getContentDisposition(), "Content-Disposition 不应为空");
        assertTrue(response.getHeaders().getContentDisposition().toString()
                .contains("attachment"), "Content-Disposition 应包含attachment");
        assertTrue(response.getHeaders().getContentDisposition().toString()
                .contains("movies_export_"), "文件名应包含'movies_export_'");

        // 4. 验证响应体不为空
        assertNotNull(response.getBody(), "响应体不应为空");
        assertTrue(response.getBody().length > 0, "Excel文件内容不应为空");

        // 5. 验证Excel文件内容
        try (ByteArrayInputStream bis = new ByteArrayInputStream(response.getBody());
             Workbook workbook = new XSSFWorkbook(bis)) {

            Sheet sheet = workbook.getSheetAt(0);
            int rowCount = sheet.getPhysicalNumberOfRows();

            // 至少应该有标题行
            assertTrue(rowCount >= 1, "Excel文件至少应包含标题行");

            // 验证标题行
            Row headerRow = sheet.getRow(0);
            assertEquals("ID", headerRow.getCell(0).getStringCellValue());
            assertEquals("电影名称", headerRow.getCell(1).getStringCellValue());

            System.out.println("===== Excel导出测试 =====");
            System.out.println("总行数：" + rowCount);

            // 如果有数据行，输出第一行数据用于测试查看
            if (rowCount > 1) {
                Row firstDataRow = sheet.getRow(1);
                System.out.println("第一行电影数据：");
                System.out.println("ID: " + firstDataRow.getCell(0).getNumericCellValue());
                System.out.println("电影名称: " + firstDataRow.getCell(1).getStringCellValue());
                System.out.println("导演: " + firstDataRow.getCell(2).getStringCellValue());
                System.out.println("类型: " + firstDataRow.getCell(4).getStringCellValue());
                System.out.println("地区: " + firstDataRow.getCell(5).getStringCellValue());
            }
        }
    }
    
    /**
     * 集成测试：测试按地区筛选导出电影数据功能
     */
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testExportMoviesByRegion() throws IOException {
        // 目标地区
        String targetRegion = "中国";

        // 1. 调用控制器方法，筛选中国地区的电影
        ResponseEntity<byte[]> response = adminReportController.exportMoviesExcel(targetRegion, null);

        // 2. 验证基本响应
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        // 3. 验证Excel内容
        try (ByteArrayInputStream bis = new ByteArrayInputStream(response.getBody());
             Workbook workbook = new XSSFWorkbook(bis)) {

            Sheet sheet = workbook.getSheetAt(0);
            int rowCount = sheet.getPhysicalNumberOfRows();

            System.out.println("===== Excel导出测试 - 中国地区电影 =====");
            System.out.println("总行数：" + rowCount);

            // 验证所有数据行的地区都是中国
            boolean allMatchRegion = true;
            int matchCount = 0;

            // 从第1行开始（跳过标题行）
            for (int i = 1; i < rowCount; i++) {
                Row row = sheet.getRow(i);
                String region = row.getCell(5).getStringCellValue();
                if (!targetRegion.equals(region)) {
                    allMatchRegion = false;
                    break;
                }
                matchCount++;
                System.out.println("电影 " + row.getCell(1).getStringCellValue() + " 地区: " + region);
            }

            assertTrue(allMatchRegion, "所有电影的地区都应该是" + targetRegion);
            System.out.println("匹配的电影数量：" + matchCount);
        }
    }

    /**
     * 集成测试：测试按类型ID筛选导出电影Excel功能
     */
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testExportMoviesByTypeIdExcel() throws IOException {
        // 选择一个存在的类型ID（假设类型ID为1存在）
        Long targetTypeId = 1L;

        // 1. 调用控制器方法，按类型ID筛选
        ResponseEntity<byte[]> response = adminReportController.exportMoviesExcel(null, targetTypeId);

        // 2. 验证基本响应
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        // 3. 从数据库获取目标类型的名称，用于日志显示
        List<Movie> moviesWithTargetType = movieMapper.findMoviesByCondition(null, targetTypeId);
        String typeName = "";
        if (!moviesWithTargetType.isEmpty() &&
            moviesWithTargetType.get(0).getMovieTypes() != null &&
            !moviesWithTargetType.get(0).getMovieTypes().isEmpty()) {
            for (MovieType type : moviesWithTargetType.get(0).getMovieTypes()) {
                if (type.getId().equals(targetTypeId)) {
                    typeName = type.getName();
                    break;
                }
            }
        }

        // 4. 验证Excel内容
        try (ByteArrayInputStream bis = new ByteArrayInputStream(response.getBody());
             Workbook workbook = new XSSFWorkbook(bis)) {

            Sheet sheet = workbook.getSheetAt(0);
            int rowCount = sheet.getPhysicalNumberOfRows();

            System.out.println("===== Excel导出测试 - 类型ID=" + targetTypeId + (typeName.isEmpty() ? "" : " (" + typeName + ")") + " =====");
            System.out.println("总行数：" + rowCount);

            // 导出的电影数量应该与查询结果一致
            assertEquals(moviesWithTargetType.size() + 1, rowCount, "Excel中的行数应该等于符合条件的电影数量加上标题行");
        }
    }

    /**
     * 集成测试：测试同时按地区和类型ID筛选导出电影Excel功能
     */
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testExportMoviesByRegionAndTypeIdExcel() throws IOException {
        // 选择一个存在的地区和类型ID
        String targetRegion = "中国";
        Long targetTypeId = 1L;

        // 1. 调用控制器方法，同时按地区和类型ID筛选
        ResponseEntity<byte[]> response = adminReportController.exportMoviesExcel(targetRegion, targetTypeId);

        // 2. 验证基本响应
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        // 3. 从数据库获取符合条件的电影
        List<Movie> filteredMovies = movieMapper.findMoviesByCondition(targetRegion, targetTypeId);
        String typeName = "";
        if (!filteredMovies.isEmpty() &&
            filteredMovies.get(0).getMovieTypes() != null &&
            !filteredMovies.get(0).getMovieTypes().isEmpty()) {
            for (MovieType type : filteredMovies.get(0).getMovieTypes()) {
                if (type.getId().equals(targetTypeId)) {
                    typeName = type.getName();
                    break;
                }
            }
        }

        // 4. 验证Excel内容
        try (ByteArrayInputStream bis = new ByteArrayInputStream(response.getBody());
             Workbook workbook = new XSSFWorkbook(bis)) {

            Sheet sheet = workbook.getSheetAt(0);
            int rowCount = sheet.getPhysicalNumberOfRows();

            System.out.println("===== Excel导出测试 - 地区=" + targetRegion + ", 类型ID=" + targetTypeId + (typeName.isEmpty() ? "" : " (" + typeName + ")") + " =====");
            System.out.println("总行数：" + rowCount);

            // 导出的电影数量应该与查询结果一致
            assertEquals(filteredMovies.size() + 1, rowCount, "Excel中的行数应该等于符合条件的电影数量加上标题行");

            // 验证所有数据行的地区和类型都匹配
            boolean allMatch = true;
            int matchCount = 0;

            // 从第1行开始（跳过标题行）
            for (int i = 1; i < rowCount; i++) {
                Row row = sheet.getRow(i);
                String region = row.getCell(5).getStringCellValue();
                if (!targetRegion.equals(region)) {
                    allMatch = false;
                    break;
                }
                matchCount++;
            }

            assertTrue(allMatch, "所有电影的地区都应该是" + targetRegion);
            System.out.println("匹配的电影数量：" + matchCount);
        }
    }
    
    /**
     * 集成测试：测试获取播放量排行榜功能
     */
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testGetPlayCountLeaderboard() {
        // 1. 使用默认参数调用控制器方法（top=10）
        Result<MovieLeaderboardDTO> result = adminReportController.getPlayCountLeaderboard(null);
        
        // 2. 验证返回结果
        assertEquals(200, result.getCode().intValue(), "响应状态码应为200");
        assertNotNull(result.getData(), "响应数据不应为空");
        
        // 3. 验证排行榜数据
        MovieLeaderboardDTO leaderboard = result.getData();
        assertNotNull(leaderboard.getItems(), "排行榜项不应为空");
        
        // 4. 打印排行榜数据用于测试查看
        System.out.println("===== 播放量排行榜测试 (默认top=10) =====");
        System.out.println("排行榜项数量：" + leaderboard.getItems().size());
        
        // 列出排行榜中的电影
        for (MovieLeaderboardDTO.MovieRankItem item : leaderboard.getItems()) {
            System.out.println(String.format("排名 #%d: %s (ID: %d) - 播放量: %d, 评分: %.1f, 地区: %s", 
                    item.getRank(), item.getTitle(), item.getId(), item.getPlayCount(), 
                    item.getScore(), item.getRegion()));
        }
        
        // 5. 测试自定义数量参数（top=5）
        Result<MovieLeaderboardDTO> result2 = adminReportController.getPlayCountLeaderboard(5);
        assertEquals(200, result2.getCode().intValue(), "响应状态码应为200");
        assertNotNull(result2.getData(), "响应数据不应为空");
        
        // 验证返回的电影数量不超过指定值
        assertTrue(result2.getData().getItems().size() <= 5, "返回的排行榜项数量不应超过指定值");
        
        System.out.println("===== 播放量排行榜测试 (top=5) =====");
        System.out.println("排行榜项数量：" + result2.getData().getItems().size());
    }

    @SpringBootTest(webEnvironment = WebEnvironment.MOCK)
    public static class ReportControllerUnitTest {

        @MockBean
        private ReportService mockReportService;

        @Autowired
        private AdminReportController adminReportController;

        @Test
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        public void testExportMoviesExcelWithMockService() throws IOException {
            // 1. 准备模拟的Excel工作簿
            Workbook mockWorkbook = new XSSFWorkbook();
            Sheet sheet = mockWorkbook.createSheet("测试数据");
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("电影名称");

            Row dataRow = sheet.createRow(1);
            dataRow.createCell(0).setCellValue(1);
            dataRow.createCell(1).setCellValue("模拟电影");

            // 2. 设置Mock行为
            when(mockReportService.exportMoviesToExcel(any(), any())).thenReturn(mockWorkbook);

            // 3. 调用控制器方法
            ResponseEntity<byte[]> response = adminReportController.exportMoviesExcel("测试地区", 999L);

            // 4. 验证基本响应
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertTrue(response.getBody().length > 0);

            // 5. 验证响应头
            assertTrue(response.getHeaders().getContentDisposition().toString().contains("movies_export_"));
        }
        
        /**
         * 单元测试：使用Mock服务测试播放量排行榜API
         */
        @Test
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        public void testGetPlayCountLeaderboardWithMockService() {
            // 1. 准备模拟的排行榜数据
            List<MovieLeaderboardDTO.MovieRankItem> mockItems = new ArrayList<>();
            mockItems.add(new MovieLeaderboardDTO.MovieRankItem(1, 1L, "测试电影1", 1000, 9.0, "中国", "/images/1.jpg"));
            mockItems.add(new MovieLeaderboardDTO.MovieRankItem(2, 2L, "测试电影2", 800, 8.5, "美国", "/images/2.jpg"));
            MovieLeaderboardDTO mockLeaderboard = new MovieLeaderboardDTO(mockItems);
            
            // 2. 设置Mock行为
            when(mockReportService.getPlayCountLeaderboard(any())).thenReturn(mockLeaderboard);
            
            // 3. 调用控制器方法
            Result<MovieLeaderboardDTO> result = adminReportController.getPlayCountLeaderboard(10);
            
            // 4. 验证结果
            assertEquals(200, result.getCode().intValue());
            assertEquals(2, result.getData().getItems().size());
            assertEquals("测试电影1", result.getData().getItems().get(0).getTitle());
            assertEquals(1000, result.getData().getItems().get(0).getPlayCount().intValue());
        }
    }
}