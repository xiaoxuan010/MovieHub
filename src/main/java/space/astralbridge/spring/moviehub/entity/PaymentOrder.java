package space.astralbridge.spring.moviehub.entity;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("payment_order")
public class PaymentOrder implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String orderNo;  // 订单号
    private Long userId;  // 用户ID
    private BigDecimal amount;  // 金额
    private Integer status;  // 状态：0-待支付，1-支付成功，2-支付失败
    private String payType;  // 支付类型
    private String tradeNo;  // 支付宝交易号
    private String vipDuration;  // VIP时长：monthly/yearly
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
} 