package space.astralbridge.spring.moviehub.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

import space.astralbridge.spring.moviehub.entity.Comment;
import space.astralbridge.spring.moviehub.security.UserDetailsImpl;

public interface CommentService extends IService<Comment> {

    /**
     * 添加评论
     * @param comment 评论对象 (包含 userId, movieId, content)
     * @return 保存后的评论对象 (包含 id 和 createTime)
     */
    Comment addComment(Comment comment);

    /**
     * 删除评论
     * @param commentId 要删除的评论 ID
     * @param currentUser 当前登录用户的信息
     * @return 是否删除成功
     * @throws SecurityException 如果用户无权删除该评论
     */
    boolean deleteComment(Long commentId, UserDetailsImpl currentUser);

    /**
     * 根据电影ID获取评论列表
     * 
     * @param movieId 电影ID
     * @return 评论列表
     */
    List<Comment> getCommentsByMovieId(Long movieId);

    /**
     * 根据用户ID获取评论列表
     * 
     * @param userId 用户ID
     * @return 评论列表
     */
    List<Comment> getCommentsByUserId(Long userId);
}