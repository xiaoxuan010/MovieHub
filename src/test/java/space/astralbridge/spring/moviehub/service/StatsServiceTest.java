package space.astralbridge.spring.moviehub.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import space.astralbridge.spring.moviehub.dto.MovieTypeCountDTO;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class StatsServiceTest {

    @Autowired
    private StatsService statsService;
    
    @Test
    public void testGetMovieCountByType() {
        // 执行方法
        MovieTypeCountDTO result = statsService.getMovieCountByType();
        
        // 验证结果不为空
        assertNotNull(result, "返回结果不应为空");
        
        // 验证类型列表不为空
        assertNotNull(result.getTypes(), "类型列表不应为空");
        
        // 验证数量列表不为空
        assertNotNull(result.getCounts(), "数量列表不应为空");
        
        // 验证两个列表长度相等
        assertEquals(result.getTypes().size(), result.getCounts().size(), 
                "类型列表和数量列表长度应相等");
        
        // 输出结果，便于查看
        System.out.println("===== 电影类型统计结果 =====");
        for (int i = 0; i < result.getTypes().size(); i++) {
            System.out.println(result.getTypes().get(i) + ": " + result.getCounts().get(i));
        }
    }
} 