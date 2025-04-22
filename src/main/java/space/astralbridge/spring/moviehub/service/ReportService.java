package space.astralbridge.spring.moviehub.service;

import org.apache.poi.ss.usermodel.Workbook;
import space.astralbridge.spring.moviehub.dto.MovieLeaderboardDTO;

/**
 * 报表服务接口
 * 处理数据导出和报表生成相关功能
 */
public interface ReportService {
    
    /**
     * 导出电影数据到Excel
     * @param region 可选筛选参数：地区
     * @param typeId 可选筛选参数：类型ID
     * @return 包含电影数据的Excel工作簿
     */
    Workbook exportMoviesToExcel(String region, Long typeId);
    
    /**
     * 获取播放量排行榜数据
     * @param top 指定返回的电影数量，默认为10
     * @return 播放量排行榜数据
     */
    MovieLeaderboardDTO getPlayCountLeaderboard(Integer top);
} 