package space.astralbridge.spring.moviehub.controller.admin;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import space.astralbridge.spring.moviehub.common.Result;
import space.astralbridge.spring.moviehub.dto.MovieTypeCountDTO;
import space.astralbridge.spring.moviehub.service.StatsService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AdminStatsControllerTest {

    @Autowired
    private AdminStatsController adminStatsController;
    
    @Autowired
    private StatsService statsService;
    
    @Test
    public void testGetMovieCountByTypeDirectly() {
        // 测试服务层方法
        MovieTypeCountDTO serviceData = statsService.getMovieCountByType();
        assertNotNull(serviceData);
        assertNotNull(serviceData.getTypes());
        assertNotNull(serviceData.getCounts());
        assertEquals(serviceData.getTypes().size(), serviceData.getCounts().size());
        
        // 输出服务层结果
        System.out.println("=== 服务层统计结果 ===");
        for (int i = 0; i < serviceData.getTypes().size(); i++) {
            System.out.println(serviceData.getTypes().get(i) + ": " + serviceData.getCounts().get(i));
        }
        
        // 测试控制器方法
        Result<MovieTypeCountDTO> result = adminStatsController.getMovieCountByType();
        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertEquals("成功", result.getMessage());
        assertNotNull(result.getData());
        
        // 输出控制器结果
        System.out.println("=== 控制器返回结果 ===");
        System.out.println("Code: " + result.getCode());
        System.out.println("Message: " + result.getMessage());
        
        MovieTypeCountDTO data = result.getData();
        for (int i = 0; i < data.getTypes().size(); i++) {
            System.out.println(data.getTypes().get(i) + ": " + data.getCounts().get(i));
        }
    }
} 