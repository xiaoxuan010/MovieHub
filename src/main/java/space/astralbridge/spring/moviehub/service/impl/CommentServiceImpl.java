package space.astralbridge.spring.moviehub.service.impl;

import java.io.Serializable;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import lombok.RequiredArgsConstructor;
import space.astralbridge.spring.moviehub.common.utils.RedisTemplateUtils;
import space.astralbridge.spring.moviehub.entity.Comment;
import space.astralbridge.spring.moviehub.mapper.CommentMapper;
import space.astralbridge.spring.moviehub.security.UserDetailsImpl;
import space.astralbridge.spring.moviehub.service.CommentService;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
    private final RedisTemplateUtils redisTemplateUtils;
    private static final Logger log = LoggerFactory.getLogger(CommentServiceImpl.class);

    @Override
    public boolean save(Comment entity) {
        redisTemplateUtils.evictCacheByPrefix("comments:all");
        redisTemplateUtils.evictCacheByPrefix("comments:movie:" + entity.getMovieId());
        redisTemplateUtils.evictCacheByPrefix("comments:user:" + entity.getUserId());
        return super.save(entity);
    }

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
    public boolean updateById(Comment entity) {
        redisTemplateUtils.evictCacheByPrefix("comments:all");
        redisTemplateUtils.evictCacheByPrefix("comments:movie:" + entity.getMovieId());
        redisTemplateUtils.evictCacheByPrefix("comments:user:" + entity.getUserId());
        redisTemplateUtils.evictCacheByPrefix("comments:id:" + entity.getId());
        return super.updateById(entity);
    }

    @Override
    public boolean deleteComment(Long commentId, UserDetailsImpl currentUser) {
        Comment comment = this.getById(commentId);
        if (comment == null) {
            log.warn("尝试删除不存在的评论: id={}", commentId);
            return true;
        }

        redisTemplateUtils.evictCacheByPrefix("comments:all");
        redisTemplateUtils.evictCacheByPrefix("comments:movie:" + comment.getMovieId());
        redisTemplateUtils.evictCacheByPrefix("comments:user:" + comment.getUserId());
        redisTemplateUtils.evictCacheByPrefix("comments:id:" + commentId);

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

    @Override
    @Cacheable(value = "comments:movie", key = "#movieId")
    public List<Comment> getCommentsByMovieId(Long movieId) {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("movie_id", movieId);
        queryWrapper.orderByDesc("create_time");
        return this.list(queryWrapper);
    }

    @Override
    @Cacheable(value = "comments:user", key = "#userId")
    public List<Comment> getCommentsByUserId(Long userId) {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.orderByDesc("create_time");
        return this.list(queryWrapper);
    }

    @Override
    @Cacheable(value = "comments:id", key = "#id")
    public Comment getById(Serializable id) {
        return super.getById(id);
    }

    @Override
    @Cacheable(value = "comments:all")
    public List<Comment> list() {
        return super.list();
    }
}