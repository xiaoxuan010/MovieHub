package space.astralbridge.spring.moviehub.service;

import space.astralbridge.spring.moviehub.dto.PaymentRequest;
import space.astralbridge.spring.moviehub.entity.PaymentOrder;

public interface PaymentService {
    /**
     * 创建支付宝支付表单
     * @param userId 用户ID
     * @param request 支付请求参数
     * @return 支付表单HTML
     */
    String createAlipayForm(Long userId, PaymentRequest request);
    
    /**
     * 处理支付宝异步通知
     * @param params 支付宝通知参数
     * @return 处理结果，成功返回"success"，失败返回"failure"
     */
    String handleAlipayNotify(java.util.Map<String, String> params);
    
    /**
     * 根据订单号查询订单
     * @param orderNo 订单号
     * @return 订单信息
     */
    PaymentOrder getOrderByOrderNo(String orderNo);
} 