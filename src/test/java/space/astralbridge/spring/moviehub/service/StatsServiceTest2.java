package space.astralbridge.spring.moviehub.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import space.astralbridge.spring.moviehub.dto.MoviePlayCountDTO;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class StatsServiceTest2 {

    @Autowired
    private StatsService statsService;
    
    @Test
    public void testGetTopMoviesByPlayCount() {
        // 测试默认值 (5)
        MoviePlayCountDTO result = statsService.getTopMoviesByPlayCount(null);
        
        // 验证结果不为空
        assertNotNull(result, "返回结果不应为空");
        
        // 验证标题列表不为空
        assertNotNull(result.getTitles(), "标题列表不应为空");
        
        // 验证播放量列表不为空
        assertNotNull(result.getPlayCounts(), "播放量列表不应为空");
        
        // 验证两个列表长度相等
        assertEquals(result.getTitles().size(), result.getPlayCounts().size(), 
                "标题列表和播放量列表长度应相等");
        
        // 输出结果，便于查看
        System.out.println("===== 热门电影统计结果 (默认top=5) =====");
        for (int i = 0; i < result.getTitles().size(); i++) {
            System.out.println(result.getTitles().get(i) + ": " + result.getPlayCounts().get(i));
        }
        
        // 测试自定义值 (3)
        MoviePlayCountDTO result2 = statsService.getTopMoviesByPlayCount(3);
        
        // 验证结果不为空
        assertNotNull(result2, "返回结果不应为空");
        
        // 验证返回的电影数量不超过指定值
        assertTrue(result2.getTitles().size() <= 3, "返回的电影数量不应超过指定值");
        
        // 输出结果，便于查看
        System.out.println("===== 热门电影统计结果 (top=3) =====");
        for (int i = 0; i < result2.getTitles().size(); i++) {
            System.out.println(result2.getTitles().get(i) + ": " + result2.getPlayCounts().get(i));
        }
    }
}
