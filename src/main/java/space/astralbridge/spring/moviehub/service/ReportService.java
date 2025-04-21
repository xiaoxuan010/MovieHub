package space.astralbridge.spring.moviehub.service;

import org.apache.poi.ss.usermodel.Workbook;

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
} 