package space.astralbridge.spring.moviehub.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import space.astralbridge.spring.moviehub.common.Result;
import space.astralbridge.spring.moviehub.common.ResultCode;
import space.astralbridge.spring.moviehub.dto.CommentDto;
import space.astralbridge.spring.moviehub.dto.CreateCommentRequest;
import space.astralbridge.spring.moviehub.security.UserDetailsImpl; // Ensure this path is correct
import space.astralbridge.spring.moviehub.service.CommentService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 评论 API 控制器
 * REST Controller for managing movie comments.
 */
@RestController
@RequestMapping("/api") // Base path for comment-related APIs
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * 获取指定电影的评论列表
     * GET endpoint to retrieve comments for a specific movie.
     * Accessible to anyone (guests included).
     *
     * @param movieId The ID of the movie.
     * @return Result containing a list of comments.
     */
    @GetMapping("/movies/{movieId}/comments")
    public Result<List<CommentDto>> getMovieComments(@PathVariable Long movieId) {
        // 调用 Service 层方法，错误处理在 Service 层
        return commentService.getCommentsByMovieId(movieId);
    }

    /**
     * 为指定电影添加评论
     * POST endpoint to add a new comment to a movie.
     * Requires authenticated user (ROLE_USER or ROLE_ADMIN).
     *
     * @param movieId The ID of the movie (from path, consistency check).
     * @param request The comment creation request body.
     * @return Result containing the newly created comment DTO.
     */
    @PostMapping("/movies/{movieId}/comments")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')") // Requires authentication
    public Result<CommentDto> addComment(@PathVariable Long movieId, @Valid @RequestBody CreateCommentRequest request) {
        // 检查路径变量和请求体中的 movieId 是否一致
        // Check if movieId in path matches movieId in request body
        if (!movieId.equals(request.getMovieId())) {
            // 使用 Result.fail(ResultCode.VALIDATE_FAILED) 根据项目定义
            // Use Result.fail(ResultCode.VALIDATE_FAILED) according to project definition
            return Result.fail(ResultCode.VALIDATE_FAILED);
        }

        UserDetailsImpl userDetails = getUserDetails();
        if (userDetails == null) {
            // 理论上 @PreAuthorize 会阻止这种情况，但以防万一
            // Theoretically @PreAuthorize prevents this, but just in case
            return Result.fail(ResultCode.UNAUTHORIZED);
        }
        // 调用 Service 层方法
        // Call the Service layer method
        return commentService.addComment(request, userDetails.getId());
    }

    /**
     * 删除评论
     * DELETE endpoint to remove a comment.
     * Requires the user to be the owner of the comment OR an admin.
     * Authorization is handled within the service layer.
     *
     * @param commentId The ID of the comment to delete.
     * @return Result indicating success or failure.
     */
    @DeleteMapping("/comments/{commentId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')") // Requires authentication
    public Result<Void> deleteComment(@PathVariable Long commentId) {
        UserDetailsImpl userDetails = getUserDetails();
        if (userDetails == null) {
            return Result.fail(ResultCode.UNAUTHORIZED);
        }

        // 提取角色用于 Service 层权限检查
        // Extract roles for Service layer authorization check
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        // 调用 Service 层方法
        // Call the Service layer method
        return commentService.deleteComment(commentId, userDetails.getId(), roles);
    }

    /**
     * Helper method to get UserDetails from Security Context.
     *
     * @return UserDetailsImpl or null if not authenticated.
     */
    private UserDetailsImpl getUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
            return (UserDetailsImpl) authentication.getPrincipal();
        }
        return null;
    }
}
