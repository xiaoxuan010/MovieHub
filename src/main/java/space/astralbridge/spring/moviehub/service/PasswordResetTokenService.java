package space.astralbridge.spring.moviehub.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import lombok.RequiredArgsConstructor;
import space.astralbridge.spring.moviehub.entity.PasswordResetToken;
import space.astralbridge.spring.moviehub.mapper.PasswordResetTokenMapper;

@Service
@RequiredArgsConstructor
public class PasswordResetTokenService extends ServiceImpl<PasswordResetTokenMapper, PasswordResetToken> {
    public PasswordResetToken createResetToken(Long uid) {
        PasswordResetToken token = new PasswordResetToken();
        token.setUserId(uid);
        token.setToken(UUID.randomUUID().toString());
        token.setExpiration(LocalDateTime.now().plusHours(1));
        getBaseMapper().insert(token);
        return token;
    }
}
