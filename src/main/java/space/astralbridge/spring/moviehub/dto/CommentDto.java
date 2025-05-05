package space.astralbridge.spring.moviehub.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 评论数据传输对象
 * Data Transfer Object for Comment.
 *
 * @author Gemini
 */
@Data
public class CommentDto {

    /**
     * 评论ID
     * Comment ID.
     */
    private Long id;

    /**
     * 用户ID
     * User ID of the commenter.
     */
    private Long userId;

    /**
     * 用户名 (Optional, can be fetched separately if needed)
     * Username of the commenter (Optional, can be fetched separately if needed).
     */
    private String username; // You might want to add username here

    /**
     * 电影ID
     * Movie ID the comment belongs to.
     */
    private Long movieId;

    /**
     * 评论内容
     * Content of the comment.
     */
    private String content;

    /**
     * 创建时间
     * Creation timestamp.
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     * Update timestamp.
     */
    private LocalDateTime updatedAt;

    // You can add more fields from the User entity if needed, like avatar URL.
    // private String userAvatar;
}
