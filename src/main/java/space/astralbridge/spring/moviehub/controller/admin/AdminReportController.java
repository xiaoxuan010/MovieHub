package space.astralbridge.spring.moviehub.controller.admin;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import space.astralbridge.spring.moviehub.common.Result;
import space.astralbridge.spring.moviehub.dto.MovieLeaderboardDTO;
import space.astralbridge.spring.moviehub.service.ReportService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 管理员报表控制器
 * 处理报表生成和数据导出功能
 */
@RestController
@RequestMapping("/api/admin/reports")
@PreAuthorize("hasRole('ADMIN')")
public class AdminReportController {

    @Autowired
    private ReportService reportService;
    
    /**
     * 导出电影数据Excel报表
     * 
     * @param region 可选筛选参数：地区
     * @param typeId 可选筛选参数：类型ID
     * @return Excel文件的二进制数据流
     * @throws IOException 如果Excel生成过程中出现IO异常
     */
    @GetMapping("/export/movies")
    public ResponseEntity<byte[]> exportMoviesExcel(
            @RequestParam(required = false) String region,
            @RequestParam(required = false) Long typeId) throws IOException {
        
        // 1. 生成Excel工作簿
        Workbook workbook = reportService.exportMoviesToExcel(region, typeId);
        
        // 2. 设置文件名（包含时间戳）
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String filename = "movies_export_" + dateFormat.format(new Date()) + ".xlsx";
        
        // 3. 将工作簿转换为字节数组
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        
        // 4. 设置HTTP响应头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.setContentDispositionFormData("attachment", filename);
        
        // 5. 返回响应实体
        return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);
    }
    
    /**
     * 获取播放量 Top N 榜单数据
     * 
     * @param top 指定返回的电影数量，默认为10
     * @return 按播放量降序排列的Top N电影列表，包含排名和关键信息
     */
    @GetMapping("/leaderboard/play_count")
    public Result<MovieLeaderboardDTO> getPlayCountLeaderboard(
            @RequestParam(value = "top", required = false, defaultValue = "10") Integer top) {
        try {
            MovieLeaderboardDTO leaderboard = reportService.getPlayCountLeaderboard(top);
            return Result.success(leaderboard);
        } catch (Exception e) {
            return Result.fail("获取播放量排行榜数据失败: " + e.getMessage());
        }
    }
} 