package space.astralbridge.spring.moviehub.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import space.astralbridge.spring.moviehub.common.Result;
import space.astralbridge.spring.moviehub.common.ResultCode;
import space.astralbridge.spring.moviehub.dto.LoginRequest;
import space.astralbridge.spring.moviehub.dto.RegisterRequest;
import space.astralbridge.spring.moviehub.entity.User;
import space.astralbridge.spring.moviehub.service.UserService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public Result<User> register(@Valid @RequestBody RegisterRequest registerRequest) {
        User user;
        try {
            user = userService.register(
                    registerRequest.getUsername(),
                    registerRequest.getPassword(),
                    registerRequest.getEmail());
        } catch (RuntimeException e) {
            return Result.fail(ResultCode.VALIDATE_FAILED, e.getMessage());
        }
        return Result.success(user);
    }

    @PostMapping("/login")
    public Result<Map<String, String>> login(@Valid @RequestBody LoginRequest loginRequest) {
        String token;
        try {
            token = userService.login(loginRequest.getUsername(), loginRequest.getPassword());
        } catch (AuthenticationException e) {
            return Result.fail(ResultCode.VALIDATE_FAILED, e.getMessage());
        }
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        return Result.success(tokenMap);
    }
}
