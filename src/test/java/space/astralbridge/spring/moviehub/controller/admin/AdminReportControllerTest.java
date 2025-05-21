package space.astralbridge.spring.moviehub.controller.admin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import space.astralbridge.spring.moviehub.common.Result;
import space.astralbridge.spring.moviehub.dto.MovieLeaderboardDTO;
import space.astralbridge.spring.moviehub.entity.Movie;
import space.astralbridge.spring.moviehub.mapper.MovieMapper;
import space.astralbridge.spring.moviehub.service.ReportService;

/**
 * 电影数据报表功能测试类
 */
@SpringBootTest
@Transactional
@DisplayName("报表控制器集成测试")
public class AdminReportControllerTest {

    @Autowired
    private AdminReportController adminReportController;

    @Autowired
    private MovieMapper movieMapper;

    /**
     * 集成测试：测试导出所有电影Excel功能
     */
    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("导出所有电影Excel")
    public void shouldExportAllMoviesExcel() throws IOException {
        // 调用控制器方法（不带筛选条件，导出所有电影）
        ResponseEntity<byte[]> response = adminReportController.exportMoviesExcel(null, null);

        // 验证响应状态码
        assertEquals(HttpStatus.OK, response.getStatusCode(), "响应状态码应为200 OK");

        // 验证响应头
        assertNotNull(response.getHeaders().getContentType(), "Content-Type 不应为空");
        assertTrue(response.getHeaders().getContentType().toString()
                .contains("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
                "Content-Type 应为Excel格式");

        assertNotNull(response.getHeaders().getContentDisposition(), "Content-Disposition 不应为空");
        assertTrue(response.getHeaders().getContentDisposition().toString()
                .contains("attachment"), "Content-Disposition 应包含attachment");
        assertTrue(response.getHeaders().getContentDisposition().toString()
                .contains("movies_export_"), "文件名应包含'movies_export_'");

        // 验证响应体不为空
        assertNotNull(response.getBody(), "响应体不应为空");
        assertTrue(response.getBody().length > 0, "Excel文件内容不应为空");

        // 验证Excel文件内容
        try (ByteArrayInputStream bis = new ByteArrayInputStream(response.getBody());
                Workbook workbook = new XSSFWorkbook(bis)) {

            Sheet sheet = workbook.getSheetAt(0);
            int rowCount = sheet.getPhysicalNumberOfRows();

            // 至少应该有标题行
            assertTrue(rowCount >= 1, "Excel文件至少应包含标题行");

            // 验证标题行
            Row headerRow = sheet.getRow(0);
            assertEquals("ID", headerRow.getCell(0).getStringCellValue(), "第一列标题应为ID");
            assertEquals("电影名称", headerRow.getCell(1).getStringCellValue(), "第二列标题应为电影名称");
        }
    }

    /**
     * 集成测试：测试按地区筛选导出电影数据功能
     */
    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("按地区筛选导出电影Excel")
    public void shouldExportMoviesByRegion() throws IOException {
        // 目标地区
        String targetRegion = "中国";

        // 调用控制器方法，筛选中国地区的电影
        ResponseEntity<byte[]> response = adminReportController.exportMoviesExcel(targetRegion, null);

        // 验证基本响应
        assertEquals(HttpStatus.OK, response.getStatusCode(), "响应状态码应为200 OK");
        assertNotNull(response.getBody(), "响应体不应为空");

        // 验证Excel内容
        try (ByteArrayInputStream bis = new ByteArrayInputStream(response.getBody());
                Workbook workbook = new XSSFWorkbook(bis)) {

            Sheet sheet = workbook.getSheetAt(0);
            int rowCount = sheet.getPhysicalNumberOfRows();

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
            }

            assertTrue(allMatchRegion, "所有电影的地区都应该是" + targetRegion);
            assertTrue(matchCount > 0 || rowCount == 1, "应至少包含标题行，如果有数据则电影数量应大于0");
        }
    }

    /**
     * 集成测试：测试按类型ID筛选导出电影Excel功能
     */
    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("按电影类型筛选导出电影Excel")
    public void shouldExportMoviesByTypeId() throws IOException {
        // 选择一个存在的类型ID（假设类型ID为1存在）
        Long targetTypeId = 1L;

        // 调用控制器方法，按类型ID筛选
        ResponseEntity<byte[]> response = adminReportController.exportMoviesExcel(null, targetTypeId);

        // 验证基本响应
        assertEquals(HttpStatus.OK, response.getStatusCode(), "响应状态码应为200 OK");
        assertNotNull(response.getBody(), "响应体不应为空");

        // 从数据库获取目标类型的电影
        List<Movie> moviesWithTargetType = movieMapper.findMoviesByCondition(null, targetTypeId);

        // 验证Excel内容
        try (ByteArrayInputStream bis = new ByteArrayInputStream(response.getBody());
                Workbook workbook = new XSSFWorkbook(bis)) {

            Sheet sheet = workbook.getSheetAt(0);
            int rowCount = sheet.getPhysicalNumberOfRows();

            // 导出的电影数量应该与查询结果一致
            assertEquals(moviesWithTargetType.size() + 1, rowCount, "Excel中的行数应该等于符合条件的电影数量加上标题行");
        }
    }

    /**
     * 集成测试：测试同时按地区和类型ID筛选导出电影Excel功能
     */
    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("按地区和电影类型筛选导出电影Excel")
    public void shouldExportMoviesByRegionAndTypeId() throws IOException {
        // 选择一个存在的地区和类型ID
        String targetRegion = "中国";
        Long targetTypeId = 1L;

        // 调用控制器方法，同时按地区和类型ID筛选
        ResponseEntity<byte[]> response = adminReportController.exportMoviesExcel(targetRegion, targetTypeId);

        // 验证基本响应
        assertEquals(HttpStatus.OK, response.getStatusCode(), "响应状态码应为200 OK");
        assertNotNull(response.getBody(), "响应体不应为空");

        // 从数据库获取符合条件的电影
        List<Movie> filteredMovies = movieMapper.findMoviesByCondition(targetRegion, targetTypeId);

        // 验证Excel内容
        try (ByteArrayInputStream bis = new ByteArrayInputStream(response.getBody());
                Workbook workbook = new XSSFWorkbook(bis)) {

            Sheet sheet = workbook.getSheetAt(0);
            int rowCount = sheet.getPhysicalNumberOfRows();

            // 导出的电影数量应该与查询结果一致
            assertEquals(filteredMovies.size() + 1, rowCount, "Excel中的行数应该等于符合条件的电影数量加上标题行");

            // 验证所有数据行的地区都匹配
            boolean allMatch = true;

            // 从第1行开始（跳过标题行）
            for (int i = 1; i < rowCount; i++) {
                Row row = sheet.getRow(i);
                String region = row.getCell(5).getStringCellValue();
                if (!targetRegion.equals(region)) {
                    allMatch = false;
                    break;
                }
            }

            assertTrue(allMatch, "所有电影的地区都应该是" + targetRegion);
        }
    }

    /**
     * 集成测试：测试获取播放量排行榜功能
     */
    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("获取播放量排行榜-默认参数")
    public void shouldGetPlayCountLeaderboardWithDefaultParams() {
        // 使用默认参数调用控制器方法（top=10）
        Result<MovieLeaderboardDTO> result = adminReportController.getPlayCountLeaderboard(null);

        // 验证返回结果
        assertEquals(200, result.getCode().intValue(), "响应状态码应为200");
        assertNotNull(result.getData(), "响应数据不应为空");

        // 验证排行榜数据
        MovieLeaderboardDTO leaderboard = result.getData();
        assertNotNull(leaderboard.getItems(), "排行榜项不应为空");
        assertTrue(leaderboard.getItems().size() <= 10, "默认返回不超过10个结果");

        // 验证排行榜顺序（播放量应该是降序排列的）
        if (leaderboard.getItems().size() >= 2) {
            Integer firstPlayCount = leaderboard.getItems().get(0).getPlayCount();
            Integer secondPlayCount = leaderboard.getItems().get(1).getPlayCount();
            assertTrue(firstPlayCount >= secondPlayCount, "播放量排行榜应按播放量降序排列");
        }
    }

    /**
     * 测试自定义数量参数
     */
    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("获取播放量排行榜-自定义数量")
    public void shouldGetPlayCountLeaderboardWithCustomLimit() {
        // 测试自定义数量参数（top=5）
        int customLimit = 5;
        Result<MovieLeaderboardDTO> result = adminReportController.getPlayCountLeaderboard(customLimit);

        // 验证返回结果
        assertEquals(200, result.getCode().intValue(), "响应状态码应为200");
        assertNotNull(result.getData(), "响应数据不应为空");

        // 验证返回的电影数量不超过指定值
        assertTrue(result.getData().getItems().size() <= customLimit,
                "返回的排行榜项数量不应超过指定值" + customLimit);
    }

    /**
     * 单元测试：使用Mock服务测试控制器
     */
    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("使用Mock服务测试Excel导出")
    public void shouldExportMoviesExcelWithMockService() throws IOException {
        // 创建模拟的ReportService
        ReportService mockReportService = mock(ReportService.class);

        // 创建待测试的控制器并注入模拟服务
        AdminReportController controllerWithMockService = new AdminReportController(mockReportService);

        // 准备模拟的Excel工作簿
        Workbook mockWorkbook = new XSSFWorkbook();
        Sheet sheet = mockWorkbook.createSheet("测试数据");
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("电影名称");

        Row dataRow = sheet.createRow(1);
        dataRow.createCell(0).setCellValue(1);
        dataRow.createCell(1).setCellValue("模拟电影");

        // 设置Mock行为
        when(mockReportService.exportMoviesToExcel(any(), any())).thenReturn(mockWorkbook);

        // 调用控制器方法
        ResponseEntity<byte[]> response = controllerWithMockService.exportMoviesExcel("测试地区", 999L);

        // 验证基本响应
        assertEquals(HttpStatus.OK, response.getStatusCode(), "响应状态码应为200 OK");
        assertNotNull(response.getBody(), "响应体不应为空");
        assertTrue(response.getBody().length > 0, "Excel文件内容不应为空");

        // 验证响应头
        assertTrue(response.getHeaders().getContentDisposition().toString().contains("movies_export_"),
                "文件名应包含'movies_export_'");
    }

    /**
     * 单元测试：使用Mock服务测试播放量排行榜API
     */
    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("使用Mock服务测试播放量排行榜")
    public void shouldGetPlayCountLeaderboardWithMockService() {
        // 创建模拟的ReportService
        ReportService mockReportService = mock(ReportService.class);

        // 创建待测试的控制器并注入模拟服务
        AdminReportController controllerWithMockService = new AdminReportController(mockReportService);

        // 准备模拟的排行榜数据
        List<MovieLeaderboardDTO.MovieRankItem> mockItems = new ArrayList<>();
        mockItems.add(new MovieLeaderboardDTO.MovieRankItem(1, 1L, "测试电影1", 1000, 9.0, "中国", "/images/1.jpg"));
        mockItems.add(new MovieLeaderboardDTO.MovieRankItem(2, 2L, "测试电影2", 800, 8.5, "美国", "/images/2.jpg"));
        MovieLeaderboardDTO mockLeaderboard = new MovieLeaderboardDTO(mockItems);

        // 设置Mock行为
        when(mockReportService.getPlayCountLeaderboard(any())).thenReturn(mockLeaderboard);

        // 调用控制器方法
        Result<MovieLeaderboardDTO> result = controllerWithMockService.getPlayCountLeaderboard(10);

        // 验证结果
        assertEquals(200, result.getCode().intValue(), "响应状态码应为200");
        assertNotNull(result.getData(), "响应数据不应为空");
        assertEquals(2, result.getData().getItems().size(), "应返回2个测试电影");
        assertEquals("测试电影1", result.getData().getItems().get(0).getTitle(), "第一部电影标题不匹配");
        assertEquals(Integer.valueOf(1000), result.getData().getItems().get(0).getPlayCount(), "第一部电影播放量不匹配");
        assertEquals("测试电影2", result.getData().getItems().get(1).getTitle(), "第二部电影标题不匹配");
        assertEquals(Integer.valueOf(800), result.getData().getItems().get(1).getPlayCount(), "第二部电影播放量不匹配");
    }
}