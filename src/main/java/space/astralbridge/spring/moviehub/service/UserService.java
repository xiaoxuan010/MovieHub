package space.astralbridge.spring.moviehub.service;

import java.io.Serializable;
import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import lombok.RequiredArgsConstructor;
import space.astralbridge.spring.moviehub.common.utils.RedisTemplateUtils;
import space.astralbridge.spring.moviehub.entity.User;
import space.astralbridge.spring.moviehub.mapper.UserMapper;
import space.astralbridge.spring.moviehub.security.JwtUtils;

@Service
@RequiredArgsConstructor
public class UserService extends ServiceImpl<UserMapper, User> {

    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplateUtils redisTemplateUtils;
    private final StringRedisTemplate redisTemplate;

    public User register(String username, String password, String email) {
        redisTemplateUtils.evictCacheByPrefix("users:");

        // 检查用户名是否已存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        if (userMapper.selectCount(queryWrapper) > 0) {
            throw new RuntimeException("用户名已存在");
        }

        // 创建新用户
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        // 默认为普通用户
        user.setUserType(0);
        // 启用状态
        user.setStatus(1);

        userMapper.insert(user);

        return user;
    }

    public String login(String username, String password) throws AuthenticationException {
        // 验证用户凭据
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        // 生成JWT token
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return jwtUtils.generateToken(userDetails);
    }

    public boolean upgradeToVip(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            return false;
        }

        user.setUserType(1); // 升级为VIP
        userMapper.updateById(user);
        return true;
    }

    public void updatePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userMapper.updateById(user);
    }

    /**
     * 根据ID获取用户，添加缓存
     */
    @Override
    @Cacheable(value = "user:id", key = "#id")
    public User getById(Serializable id) {
        return super.getById(id);
    }

    /**
     * 获取所有用户列表，添加缓存
     */
    @Override
    @Cacheable(value = "users:all")
    public List<User> list() {
        return super.list();
    }

    @Override
    public boolean updateById(User entity) {
        redisTemplateUtils.evictCacheByPrefix("users:");
        redisTemplate.delete("users:id::" + entity.getId());

        return super.updateById(entity);
    }

    @Override
    public boolean save(User entity) {
        redisTemplateUtils.evictCacheByPrefix("users:");
        return super.save(entity);
    }
}
