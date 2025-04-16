package space.astralbridge.spring.moviehub.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import space.astralbridge.spring.moviehub.common.Result;
import space.astralbridge.spring.moviehub.common.ResultCode;
import space.astralbridge.spring.moviehub.dto.ChangePasswordRequest;
import space.astralbridge.spring.moviehub.entity.User;
import space.astralbridge.spring.moviehub.security.UserDetailsImpl;
import space.astralbridge.spring.moviehub.service.UserService;

@RestController
@RequestMapping("/api/my")
@RequiredArgsConstructor
public class UserMyController {

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public Result<User> getCurrentUser(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userService.getById(userDetails.getId());
        return Result.success(user);
    }

    @PostMapping("/changePassword")
    public Result<User> changePassword(Authentication authentication,
            @RequestBody ChangePasswordRequest changePasswordRequest) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userService.getById(userDetails.getId());
        if (user == null) {
            return Result.fail(ResultCode.VALIDATE_FAILED, "用户不存在");
        }
        if (!passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())) {
            return Result.fail(ResultCode.VALIDATE_FAILED, "旧密码不正确");
        }
        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        if (userService.updateById(user)) {
            return Result.success(user);
        }
        else {
            return Result.fail("修改失败");
        }
    }

    // @PostMapping("/upgrade/{userId}")
    // public Result<Boolean> upgradeToVip(@PathVariable Long userId) {
    // boolean success = userService.upgradeToVip(userId);
    // if (success) {
    // return Result.success(true);
    // } else {
    // return Result.fail("升级失败，请检查用户是否存在");
    // }
    // }
}
