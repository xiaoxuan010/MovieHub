package space.astralbridge.spring.moviehub.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import space.astralbridge.spring.moviehub.entity.Comment;
import space.astralbridge.spring.moviehub.mapper.CommentMapper;
import space.astralbridge.spring.moviehub.security.UserDetailsImpl;
import space.astralbridge.spring.moviehub.service.CommentService;
import space.astralbridge.spring.moviehub.service.MovieService;
import space.astralbridge.spring.moviehub.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
    private static final Logger log = LoggerFactory.getLogger(CommentServiceImpl.class);
    private final MovieService movieService;
    private final UserService userService;

    @Override
    public Comment addComment(Comment comment) {
        if (comment.getMovieId() == null) {
            log.warn("尝试添加评论时 movieId 为空");
            throw new IllegalArgumentException("电影 ID 不能为空");
        }
        if (comment.getContent() == null || comment.getContent().isBlank()) {
            log.warn("尝试添加评论时 content 为空或空白");
            throw new IllegalArgumentException("评论内容不能为空");
        }
        if (comment.getUserId() == null) {
            log.warn("尝试添加评论时 userId 为空");
            throw new IllegalArgumentException("用户信息错误，无法评论");
        }
        this.save(comment);
        return comment;
    }

    @Override
    public boolean deleteComment(Long commentId, UserDetailsImpl currentUser) {
        Comment comment = this.getById(commentId);
        if (comment == null) {
            log.warn("尝试删除不存在的评论: id={}", commentId);
            return true;
        }

        log.info("用户 {} (ID: {}) 尝试删除评论: id={}", currentUser.getUsername(), currentUser.getId(), commentId);
        boolean isAdmin = currentUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        boolean isOwner = comment.getUserId().equals(currentUser.getId());

        if (isAdmin || isOwner) {
            log.info("权限检查通过 (isAdmin={}, isOwner={}), 准备删除评论 id={}", isAdmin, isOwner, commentId);
            boolean removed = this.removeById(commentId);
            if (removed) {
                log.info("评论删除成功: id={}", commentId);
            } else {
                log.error("评论删除失败（数据库操作返回 false）: id={}", commentId);
            }
            return removed;
        } else {
            log.warn("用户 {} (ID: {}) 无权删除评论 id={} (属于用户 ID: {})",
                    currentUser.getUsername(), currentUser.getId(), commentId, comment.getUserId());
            throw new AccessDeniedException("无权删除此评论");
        }
    }
}