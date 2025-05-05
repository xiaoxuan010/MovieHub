package space.astralbridge.spring.moviehub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 创建评论请求 DTO
 * DTO for creating a new comment.
 *
 * @author Gemini
 */
@Data
public class CreateCommentRequest {

    /**
     * 电影ID
     * Movie ID for the comment.
     */
    @NotNull(message = "Movie ID cannot be null")
    private Long movieId;

    /**
     * 评论内容
     * Content of the comment.
     */
    @NotBlank(message = "Comment content cannot be blank")
    @Size(max = 5000, message = "Comment content cannot exceed 5000 characters")
    private String content;
}
