package space.astralbridge.spring.moviehub.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import space.astralbridge.spring.moviehub.entity.PaymentOrder;

@Mapper
public interface PaymentOrderMapper extends BaseMapper<PaymentOrder> {
} 