package space.astralbridge.spring.moviehub.controller.admin;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import jakarta.websocket.server.PathParam;
import space.astralbridge.spring.moviehub.common.Result;
import space.astralbridge.spring.moviehub.common.ResultCode;
import space.astralbridge.spring.moviehub.dto.CreateUserRequestDto;
import space.astralbridge.spring.moviehub.entity.User;
import space.astralbridge.spring.moviehub.service.UserService;

@RestController
@RequestMapping("/api/admin/user")
public class AdminUserController {

    private final PasswordEncoder passwordEncoder;
    @Resource
    private UserService userService;

    @Resource
    private ModelMapper modelMapper;

    AdminUserController(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/all")
    public Result<List<User>> getAllUsers() {
        List<User> users = userService.list();
        return Result.success(users);
    }

    @GetMapping("/")
    public Result<User> getUserById(@PathParam("uid") Long uid) {
        User user = userService.getById(uid);
        if (user == null) {
            return Result.fail(ResultCode.VALIDATE_FAILED, "用户不存在");
        }
        return Result.success(user);
    }

    @PostMapping("/create")
    public Result<User> createUser(@RequestBody CreateUserRequestDto dto) {
        User user = modelMapper.map(dto, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (userService.save(user)) {
            return Result.success(userService.getById(user.getId()));
        } else {
            return Result.fail(ResultCode.VALIDATE_FAILED, "创建用户失败");
        }
    }

    @PostMapping("/update")
    public Result<User> updateUser(@RequestBody User user) {
        if (userService.updateById(user)) {
            return Result.success(userService.getById(user.getId()));
        } else {
            return Result.fail(ResultCode.VALIDATE_FAILED, "更新用户失败");
        }
    }

    @PostMapping("/delete")
    public Result<Void> deleteUser(@PathParam("uid") Long uid) {
        if (userService.removeById(uid)) {
            return Result.success(null);
        } else {
            return Result.fail(ResultCode.FAILED, "删除用户失败");
        }
    }

}
