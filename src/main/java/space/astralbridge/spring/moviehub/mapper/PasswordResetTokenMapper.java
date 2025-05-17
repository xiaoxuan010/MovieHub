package space.astralbridge.spring.moviehub.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import space.astralbridge.spring.moviehub.entity.PasswordResetToken;

@Mapper
public interface PasswordResetTokenMapper extends BaseMapper<PasswordResetToken> {

}
