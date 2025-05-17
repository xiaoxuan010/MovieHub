package space.astralbridge.spring.moviehub.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import space.astralbridge.spring.moviehub.common.Result;
import space.astralbridge.spring.moviehub.common.ResultCode;
import space.astralbridge.spring.moviehub.dto.LoginRequest;
import space.astralbridge.spring.moviehub.dto.PasswordResetRequest;
import space.astralbridge.spring.moviehub.dto.RegisterRequest;
import space.astralbridge.spring.moviehub.dto.RequestPasswordResetRequest;
import space.astralbridge.spring.moviehub.entity.PasswordResetToken;
import space.astralbridge.spring.moviehub.entity.User;
import space.astralbridge.spring.moviehub.service.PasswordResetTokenService;
import space.astralbridge.spring.moviehub.service.UserService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final PasswordResetTokenService passwordResetTokenService;
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String FROM_EMAIL;

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

    @PostMapping("/request-password-reset")
    public Result<Void> requestPasswordReset(@Valid @RequestBody RequestPasswordResetRequest rprDto,
            HttpServletRequest request) throws MessagingException {
        User user = userService.getOne(new QueryWrapper<User>().eq("email", rprDto.getEmail()));
        if (user == null) {
            return Result.fail(ResultCode.VALIDATE_FAILED, "用户不存在或邮箱不匹配");
        }
        PasswordResetToken token = passwordResetTokenService.createResetToken(user.getId());
        String appUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        mimeMessageHelper.setFrom("MovieHub <" + FROM_EMAIL + ">");
        mimeMessageHelper.setTo(rprDto.getEmail());
        mimeMessageHelper.setSubject("重置您的 MovieHub 密码");

        Context context = new Context();
        context.setVariable("username", user.getUsername());
        context.setVariable("token", token.getToken());
        context.setVariable("expiration", token.getExpiration());
        context.setVariable("appUrl", appUrl);

        String content = templateEngine.process("ResetPwdMailTmpl.html", context);
        mimeMessageHelper.setText(content, true);

        mailSender.send(mimeMessage);
        return Result.success(null);
    }

    @PostMapping("/reset-password")
    public Result<Void> resetPassword(@Valid @RequestBody PasswordResetRequest passwordResetRequest) {
        User user = userService.getOne(new QueryWrapper<User>().eq("username", passwordResetRequest.getUsername()));
        if (user == null) {
            return Result.fail(ResultCode.VALIDATE_FAILED, "用户不存在或用户名不匹配");
        }
        PasswordResetToken token = passwordResetTokenService.getOne(
                new QueryWrapper<PasswordResetToken>().eq("token", passwordResetRequest.getToken()));
        if (token == null || token.isExpired() || token.getUserId() != user.getId()) {
            return Result.fail(ResultCode.VALIDATE_FAILED, "无效或过期的重置令牌");
        }
        userService.updatePassword(user, passwordResetRequest.getNewPassword());
        return Result.success(null);
    }
}
