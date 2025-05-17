package space.astralbridge.spring.moviehub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AddCommentRequest {

    @NotNull(message = "电影ID不能为空")
    private Long movieId;

    @NotBlank(message = "评论内容不能为空")
    @Size(max = 200, message = "评论内容不能超过200字") // 限制评论长度
    private String content;
}