package space.astralbridge.spring.moviehub.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import space.astralbridge.spring.moviehub.common.Result;
import space.astralbridge.spring.moviehub.entity.PaymentOrder;
import space.astralbridge.spring.moviehub.mapper.PaymentOrderMapper;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/orders")
public class AdminOrderController {

    private final PaymentOrderMapper paymentOrderMapper;

    /**
     * 查询所有订单
     */
    @GetMapping
    public Result<List<PaymentOrder>> getAllOrders() {
        List<PaymentOrder> orders = paymentOrderMapper.selectList(null);
        return Result.success("查询成功", orders);
    }
} 