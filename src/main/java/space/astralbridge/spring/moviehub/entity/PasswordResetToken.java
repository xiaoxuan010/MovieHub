package space.astralbridge.spring.moviehub.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("password_reset_token")
public class PasswordResetToken {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String token;

    private LocalDateTime expiration;

    private LocalDateTime createTime;

    private Long userId;

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiration);
    }
}
