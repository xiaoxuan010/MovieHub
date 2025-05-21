package space.astralbridge.spring.moviehub.service;

import java.util.List;

import space.astralbridge.spring.moviehub.dto.PaymentRequest;
import space.astralbridge.spring.moviehub.entity.PaymentOrder;

public interface PaymentService {
    /**
     * 创建支付宝支付表单
     * 
     * @param userId  用户ID
     * @param request 支付请求参数
     * @return 支付表单HTML
     */
    String createAlipayForm(Long userId, PaymentRequest request);

    /**
     * 根据订单号查询订单
     * 
     * @param orderNo 订单号
     * @return 订单信息
     */
    PaymentOrder getOrderByOrderNo(String orderNo);

    /**
     * 根据用户ID查询订单列表
     * 
     * @param userId 用户ID
     * @return 订单列表
     */
    List<PaymentOrder> getOrdersByUserId(Long userId);

    /**
     * 更新订单信息
     * 
     * @param order 订单对象
     * @return 是否更新成功
     */
    boolean updateById(PaymentOrder order);
}