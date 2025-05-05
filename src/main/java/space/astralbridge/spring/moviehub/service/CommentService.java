package space.astralbridge.spring.moviehub.service;

import com.baomidou.mybatisplus.extension.service.IService;
import space.astralbridge.spring.moviehub.common.Result;
import space.astralbridge.spring.moviehub.dto.CommentDto;
import space.astralbridge.spring.moviehub.dto.CreateCommentRequest;
import space.astralbridge.spring.moviehub.entity.Comment;

import java.util.List;

/**
 * 评论服务接口
 * Service interface for managing comments.
 *
 * @author Gemini
 */
public interface CommentService extends IService<Comment> {

    /**
     * 添加评论
     * Adds a new comment for a movie.
     *
     * @param request CreateCommentRequest containing movie ID and content.
     * @param userId The ID of the user adding the comment.
     * @return Result containing the created CommentDto or an error.
     */
    Result<CommentDto> addComment(CreateCommentRequest request, Long userId);

    /**
     * 删除评论
     * Deletes a comment by its ID. Requires authorization check.
     *
     * @param commentId The ID of the comment to delete.
     * @param userId The ID of the user requesting the deletion.
     * @param userRoles The roles of the user requesting the deletion.
     * @return Result indicating success or failure.
     */
    Result<Void> deleteComment(Long commentId, Long userId, List<String> userRoles);

    /**
     * 获取电影的评论列表
     * Gets a list of comments for a specific movie.
     *
     * @param movieId The ID of the movie.
     * @return Result containing a list of CommentDto or an error.
     */
    Result<List<CommentDto>> getCommentsByMovieId(Long movieId);
}
