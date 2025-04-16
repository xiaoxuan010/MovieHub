package space.astralbridge.spring.moviehub.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import space.astralbridge.spring.moviehub.entity.User;
import space.astralbridge.spring.moviehub.mapper.UserMapper;
import space.astralbridge.spring.moviehub.security.JwtUtils;

@Service
public class UserService extends ServiceImpl<UserMapper, User> {

    @Resource
    private UserMapper userMapper;
    @Resource
    private AuthenticationManager authenticationManager;
    @Resource
    private UserDetailsService userDetailsService;
    @Resource
    private JwtUtils jwtUtils;
    @Resource
    private PasswordEncoder passwordEncoder;

    public User register(String username, String password, String email) {
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

    // public User getCurrentUser(String username) {
    // QueryWrapper<User> queryWrapper = new QueryWrapper<>();
    // queryWrapper.eq("username", username);
    // return userMapper.selectOne(queryWrapper);
    // }

    public boolean upgradeToVip(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            return false;
        }

        user.setUserType(1); // 升级为VIP
        userMapper.updateById(user);
        return true;
    }

}
