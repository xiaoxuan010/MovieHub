package space.astralbridge.spring.moviehub.controller.admin;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import space.astralbridge.spring.moviehub.common.Result;
import space.astralbridge.spring.moviehub.dto.MoviePlayCountDTO;
import space.astralbridge.spring.moviehub.service.StatsService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AdminStatsControllerTest2 {

    @Autowired
    private AdminStatsController adminStatsController;
    
    @Autowired
    private StatsService statsService;
    
    @Test
    public void testGetTopMoviesByPlayCount() {
        // 测试服务层方法
        MoviePlayCountDTO serviceData = statsService.getTopMoviesByPlayCount(5);
        assertNotNull(serviceData);
        assertNotNull(serviceData.getTitles());
        assertNotNull(serviceData.getPlayCounts());
        assertEquals(serviceData.getTitles().size(), serviceData.getPlayCounts().size());
        
        // 输出服务层结果
        System.out.println("=== 服务层统计结果 ===");
        for (int i = 0; i < serviceData.getTitles().size(); i++) {
            System.out.println(serviceData.getTitles().get(i) + ": " + serviceData.getPlayCounts().get(i));
        }
        
        // 测试控制器方法
        Result<MoviePlayCountDTO> result = adminStatsController.getTopMoviesByPlayCount(5);
        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertEquals("成功", result.getMessage());
        assertNotNull(result.getData());
        
        // 输出控制器结果
        System.out.println("=== 控制器返回结果 ===");
        System.out.println("Code: " + result.getCode());
        System.out.println("Message: " + result.getMessage());
        
        MoviePlayCountDTO data = result.getData();
        for (int i = 0; i < data.getTitles().size(); i++) {
            System.out.println(data.getTitles().get(i) + ": " + data.getPlayCounts().get(i));
        }
    }
}
