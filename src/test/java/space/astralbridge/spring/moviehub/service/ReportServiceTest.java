package space.astralbridge.spring.moviehub.service;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import space.astralbridge.spring.moviehub.entity.Movie;
import space.astralbridge.spring.moviehub.mapper.MovieMapper;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 报表服务(ReportService)测试类
 */
@SpringBootTest
@Transactional
public class ReportServiceTest {

    @Autowired
    private ReportService reportService;
    
    @Autowired
    private MovieMapper movieMapper;
    
    /**
     * 测试导出所有电影数据到Excel功能
     */
    @Test
    public void testExportMoviesToExcel() throws IOException {
        // 1. 调用服务导出所有电影
        Workbook workbook = reportService.exportMoviesToExcel(null, null);
        
        // 2. 验证基本Excel结构
        assertNotNull(workbook, "工作簿不应为空");
        assertEquals(1, workbook.getNumberOfSheets(), "应只有一个工作表");
        
        Sheet sheet = workbook.getSheetAt(0);
        assertNotNull(sheet, "工作表不应为空");
        assertEquals("电影数据", sheet.getSheetName(), "工作表名称应为'电影数据'");
        
        // 3. 验证标题行
        Row headerRow = sheet.getRow(0);
        assertNotNull(headerRow, "标题行不应为空");
        
        String[] expectedHeaders = {"ID", "电影名称", "导演", "演员", "类型", "地区", 
                                   "上映日期", "时长(分钟)", "是否VIP", "评分", "播放量"};
        
        for (int i = 0; i < expectedHeaders.length; i++) {
            assertEquals(expectedHeaders[i], headerRow.getCell(i).getStringCellValue(),
                    "标题第" + (i+1) + "列应为" + expectedHeaders[i]);
        }
        
        // 4. 验证数据行
        int rowCount = sheet.getPhysicalNumberOfRows();
        System.out.println("===== Excel中的电影数据 =====");
        System.out.println("总行数：" + rowCount);
        
        // 查询数据库中的电影数量，用于验证
        List<Movie> movies = movieMapper.findMoviesByCondition(null, null);
        
        // 验证数据行数应等于数据库中电影数量（加上标题行）
        assertEquals(movies.size() + 1, rowCount, 
                "Excel行数应等于数据库中电影数量加上标题行");
        
        // 打印第一行电影数据，用于检查
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
    
    /**
     * 测试按地区筛选导出电影数据功能
     */
    @Test
    public void testExportMoviesByRegion() throws IOException {
        // 目标地区
        String targetRegion = "中国";
        
        // 1. 调用服务导出指定地区电影
        Workbook workbook = reportService.exportMoviesToExcel(targetRegion, null);
        
        // 2. 验证工作簿基本结构
        assertNotNull(workbook);
        Sheet sheet = workbook.getSheetAt(0);
        
        // 3. 验证数据行
        int rowCount = sheet.getPhysicalNumberOfRows();
        System.out.println("===== 中国地区电影数据 =====");
        System.out.println("总行数：" + rowCount);
        
        // 4. 验证所有数据行的地区都匹配目标地区
        boolean allMatchRegion = true;
        int matchCount = 0;
        
        // 从第1行开始（跳过标题行）
        for (int i = 1; i < rowCount; i++) {
            Row row = sheet.getRow(i);
            String region = row.getCell(5).getStringCellValue();
            if (!targetRegion.equals(region)) {
                allMatchRegion = false;
                System.out.println("错误：电影 '" + row.getCell(1).getStringCellValue() + 
                        "' 地区是 '" + region + "' 而不是 '" + targetRegion + "'");
                break;
            }
            matchCount++;
        }
        
        assertTrue(allMatchRegion, "所有电影的地区都应该是" + targetRegion);
        System.out.println("匹配的电影数量：" + matchCount);
        
        // 5. 验证数据库查询结果与Excel行数一致
        List<Movie> moviesInRegion = movieMapper.findMoviesByCondition(targetRegion, null);
        assertEquals(moviesInRegion.size() + 1, rowCount, 
                "Excel行数应等于指定地区电影数量加上标题行");
    }
    
    /**
     * 测试按类型ID筛选导出电影数据功能
     */
    @Test
    public void testExportMoviesByTypeId() throws IOException {
        // 选择一个存在的类型ID（假设类型ID为1存在）
        Long targetTypeId = 1L;
        
        // 1. 调用服务导出指定类型电影
        Workbook workbook = reportService.exportMoviesToExcel(null, targetTypeId);
        
        // 2. 验证工作簿基本结构
        assertNotNull(workbook);
        Sheet sheet = workbook.getSheetAt(0);
        
        // 3. A验证数据行
        int rowCount = sheet.getPhysicalNumberOfRows();
        System.out.println("===== 类型ID=" + targetTypeId + " 电影数据 =====");
        System.out.println("总行数：" + rowCount);
        
        // 打印每部电影的名称和类型，便于查看
        for (int i = 1; i < rowCount; i++) {
            Row row = sheet.getRow(i);
            String title = row.getCell(1).getStringCellValue();
            String typeStr = row.getCell(4).getStringCellValue();
            System.out.println("电影：" + title + " 类型：" + typeStr);
        }
        
        // 4. 验证数据库查询结果与Excel行数一致
        List<Movie> moviesOfType = movieMapper.findMoviesByCondition(null, targetTypeId);
        assertEquals(moviesOfType.size() + 1, rowCount, 
                "Excel行数应等于指定类型电影数量加上标题行");
    }
    
    /**
     * 测试组合筛选条件导出电影数据功能
     */
    @Test
    public void testExportMoviesWithCombinedFilters() throws IOException {
        // 设定组合筛选条件
        String targetRegion = "中国";
        Long targetTypeId = 1L; // 假设类型ID为1存在
        
        // 1. 调用服务导出满足两个条件的电影
        Workbook workbook = reportService.exportMoviesToExcel(targetRegion, targetTypeId);
        
        // 2. 验证工作簿基本结构
        assertNotNull(workbook);
        Sheet sheet = workbook.getSheetAt(0);
        
        // 3. 验证数据行
        int rowCount = sheet.getPhysicalNumberOfRows();
        System.out.println("===== 中国地区且类型ID=" + targetTypeId + " 电影数据 =====");
        System.out.println("总行数：" + rowCount);
        
        // 4. 验证所有电影都满足地区条件
        boolean allMatchRegion = true;
        for (int i = 1; i < rowCount; i++) {
            Row row = sheet.getRow(i);
            String region = row.getCell(5).getStringCellValue();
            if (!targetRegion.equals(region)) {
                allMatchRegion = false;
                break;
            }
            
            String title = row.getCell(1).getStringCellValue();
            String typeStr = row.getCell(4).getStringCellValue();
            System.out.println("电影：" + title + " 地区：" + region + " 类型：" + typeStr);
        }
        
        assertTrue(allMatchRegion, "所有电影的地区都应该是" + targetRegion);
        
        // 5. 验证数据库查询结果与Excel行数一致
        List<Movie> filteredMovies = movieMapper.findMoviesByCondition(targetRegion, targetTypeId);
        assertEquals(filteredMovies.size() + 1, rowCount, 
                "Excel行数应等于满足组合条件的电影数量加上标题行");
    }
} 