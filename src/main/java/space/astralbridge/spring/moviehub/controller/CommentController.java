package space.astralbridge.spring.moviehub.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import space.astralbridge.spring.moviehub.common.Result;
import space.astralbridge.spring.moviehub.dto.AddCommentRequest;
import space.astralbridge.spring.moviehub.entity.Comment;
import space.astralbridge.spring.moviehub.security.UserDetailsImpl;
import space.astralbridge.spring.moviehub.service.CommentService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import space.astralbridge.spring.moviehub.service.UserService;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final UserDetailsService userDetailsService;

    /**
     * 获取某部电影的评论列表（分页）
     *
     * @param movieId 电影ID
     * @param current 当前页码
     * @param size    每页数量
     * @return 分页的评论列表
     */
    @GetMapping("/movie/{movieId}")
    public Result<Page<Comment>> getCommentsByMovie(
            @PathVariable Long movieId,
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size) {
        Page<Comment> page = new Page<>(current, size);
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("movie_id", movieId);
        queryWrapper.orderByDesc("create_time"); // 按创建时间降序排列
        Page<Comment> commentPage = commentService.page(page, queryWrapper);
        return Result.success(commentPage);
    }

    /**
     * 添加评论 (需要登录)
     *
     * @param request        评论请求体
     * @param authentication 当前认证信息
     * @return 添加成功的评论
     */
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public Result<Comment> addComment(@Valid @RequestBody AddCommentRequest request, Authentication authentication) {
        Object principal = authentication.getPrincipal();
        Long userId;
        if (principal instanceof UserDetailsImpl customUserDetails) {
            userId = customUserDetails.getId();
        } else if (principal instanceof UserDetails springUserDetails) {
            UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(springUserDetails.getUsername());
            userId = userDetails.getId();
        } else if (principal instanceof String username) {
            UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(username);
            userId = userDetails.getId();
        }
        else {
            log.error("无法识别的 Principal 类型: {}", principal.getClass());
            return Result.fail("无法获取用户信息");
        }
        Comment comment = new Comment(userId, request.getMovieId(), request.getContent());
        try {
            Comment savedComment = commentService.addComment(comment);
            return Result.success(savedComment);
        } catch (IllegalArgumentException e) {
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 删除评论 (需要登录，且只能删除自己的评论，或管理员删除任意评论)
     *
     * @param commentId      要删除的评论ID
     * @param authentication 当前认证信息
     * @return 删除结果
     */
    @DeleteMapping("/{commentId}")
    @PreAuthorize("isAuthenticated()")
    public Result<Void> deleteComment(@PathVariable Long commentId, Authentication authentication) {
        Object principal = authentication.getPrincipal();
        UserDetailsImpl userDetails;
        if (principal instanceof UserDetailsImpl customUserDetails) {
            userDetails = customUserDetails;
        } else if (principal instanceof UserDetails springUserDetails) {
            userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(springUserDetails.getUsername());
        } else if (principal instanceof String username) {
            userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(username);
        } else {
            log.error("无法识别的 Principal 类型: {}", principal.getClass());
            return Result.fail(space.astralbridge.spring.moviehub.common.ResultCode.UNAUTHORIZED,"无法获取用户信息");
        }

        try {
            boolean deleted = commentService.deleteComment(commentId, userDetails);
            if (deleted) {
                return Result.success();
            } else {
                return Result.fail("评论不存在或删除失败");
            }
        } catch (AccessDeniedException e) {
            return Result.fail(space.astralbridge.spring.moviehub.common.ResultCode.FORBIDDEN, "无权删除此评论");
        } catch (Exception e) {
            log.error("删除评论时出错", e);
            return Result.fail("删除评论时发生错误");
        }
    }
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(CommentController.class);

}