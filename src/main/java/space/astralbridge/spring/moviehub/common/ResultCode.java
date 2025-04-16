package space.astralbridge.spring.moviehub.common;

import lombok.Getter;

@Getter
public enum ResultCode {
    SUCCESS(200, "成功"),
    FAILED(500, "服务端报错"),
    VALIDATE_FAILED(400, "参数校验失败"),
    UNAUTHORIZED(401, "暂未登录或token已经过期"),
    FORBIDDEN(403, "没有权限访问");

    private Integer code;
    private String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
