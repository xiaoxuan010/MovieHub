package space.astralbridge.spring.moviehub.controller.admin;

import jakarta.annotation.Resource;
import jakarta.websocket.server.PathParam;
import org.springframework.web.bind.annotation.*;
import space.astralbridge.spring.moviehub.common.Result;
import space.astralbridge.spring.moviehub.common.ResultCode;
import space.astralbridge.spring.moviehub.entity.User;
import space.astralbridge.spring.moviehub.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/admin/user")
public class AdminUserController {
    @Resource
    private UserService userService;

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
    public Result<User> createUser(@RequestBody User user) {
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
