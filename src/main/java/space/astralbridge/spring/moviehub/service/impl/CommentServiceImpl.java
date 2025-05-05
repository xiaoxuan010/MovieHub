package space.astralbridge.spring.moviehub.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import space.astralbridge.spring.moviehub.common.Result;
import space.astralbridge.spring.moviehub.common.ResultCode;
import space.astralbridge.spring.moviehub.dto.CommentDto;
import space.astralbridge.spring.moviehub.dto.CreateCommentRequest;
import space.astralbridge.spring.moviehub.entity.Comment;
import space.astralbridge.spring.moviehub.entity.Movie;
import space.astralbridge.spring.moviehub.entity.User;
import space.astralbridge.spring.moviehub.mapper.CommentMapper;
import space.astralbridge.spring.moviehub.mapper.MovieMapper;
import space.astralbridge.spring.moviehub.mapper.UserMapper;
import space.astralbridge.spring.moviehub.service.CommentService; // Import the interface

import java.util.List;
import java.util.stream.Collectors;

/**
 * 评论服务实现类
 * Implementation of the CommentService interface.
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private MovieMapper movieMapper; // To check if movie exists

    @Autowired
    private UserMapper userMapper; // To get username

    @Autowired
    private ModelMapper modelMapper; // For DTO mapping

    /**
     * 添加评论
     * Adds a new comment for a movie.
     *
     * @param request CreateCommentRequest containing movie ID and content.
     * @param userId The ID of the user adding the comment.
     * @return Result containing the created CommentDto or an error.
     */
    @Override
    public Result<CommentDto> addComment(CreateCommentRequest request, Long userId) {
        // 1. 检查电影是否存在 (Check if the movie exists)
        Movie movie = movieMapper.selectById(request.getMovieId());
        if (movie == null) {
            // 使用 Result.fail(ResultCode)
            // Use Result.fail(ResultCode)
            return Result.fail(ResultCode.NOT_FOUND);
        }

        // 2. 创建并保存评论 (Create and save the comment)
        Comment comment = new Comment();
        comment.setMovieId(request.getMovieId());
        comment.setUserId(userId);
        comment.setContent(request.getContent());

        boolean saved = this.save(comment);

        if (saved) {
            // 3. 映射到 DTO 并返回 (Map to DTO and return)
            Comment newComment = comment;
            CommentDto commentDto = convertToDto(newComment);
            return Result.success(commentDto);
        } else {
            // 使用 Result.fail(ResultCode)
            // Use Result.fail(ResultCode)
            return Result.fail(ResultCode.FAILED); // 使用 FAILED 或 INTERNAL_SERVER_ERROR
        }
    }

    /**
     * 删除评论
     * Deletes a comment by its ID. Requires authorization check.
     *
     * @param commentId The ID of the comment to delete.
     * @param userId The ID of the user requesting the deletion.
     * @param userRoles The roles of the user requesting the deletion.
     * @return Result indicating success or failure.
     */
    @Override
    public Result<Void> deleteComment(Long commentId, Long userId, List<String> userRoles) {
        // 1. 查找评论 (Find the comment)
        Comment comment = commentMapper.selectById(commentId);
        if (comment == null) {
            // 使用 Result.fail(ResultCode)
            // Use Result.fail(ResultCode)
            return Result.fail(ResultCode.NOT_FOUND);
        }

        // 2. 权限检查 (Authorization check)
        boolean isAdmin = userRoles != null && userRoles.contains("ROLE_ADMIN");
        boolean isOwner = comment.getUserId().equals(userId);

        if (!isAdmin && !isOwner) {
            // 使用 Result.fail(ResultCode)
            // Use Result.fail(ResultCode)
            return Result.fail(ResultCode.FORBIDDEN);
        }

        // 3. 删除评论 (Delete the comment)
        boolean deleted = this.removeById(commentId);

        if (deleted) {
            // 使用 Result.success() 因为泛型是 Void
            // Use Result.success() because generic type is Void
            return Result.success();
        } else {
            // 使用 Result.fail(ResultCode)
            // Use Result.fail(ResultCode)
            return Result.fail(ResultCode.FAILED); // 使用 FAILED 或 INTERNAL_SERVER_ERROR
        }
    }

    /**
     * 获取电影的评论列表
     * Gets a list of comments for a specific movie.
     *
     * @param movieId The ID of the movie.
     * @return Result containing a list of CommentDto or an error.
     */
    @Override
    public Result<List<CommentDto>> getCommentsByMovieId(Long movieId) {
        // 1. 检查电影是否存在 (Check if movie exists - optional but good practice)
        Movie movie = movieMapper.selectById(movieId);
        if (movie == null) {
            // 使用 Result.fail(ResultCode)
            // Use Result.fail(ResultCode)
            // 注意：这里的泛型是 List<CommentDto>，但 fail 方法会正确处理
            // Note: The generic type here is List<CommentDto>, but the fail method handles it correctly
            return Result.fail(ResultCode.NOT_FOUND);
        }

        // 2. 查询评论 (Query comments)
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("movie_id", movieId);
        queryWrapper.orderByDesc("created_at");
        List<Comment> comments = commentMapper.selectList(queryWrapper);

        // 3. 映射到 DTO 列表 (Map to DTO list)
        List<CommentDto> commentDtos = comments.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return Result.success(commentDtos);
    }

    /**
     * 将 Comment 实体转换为 CommentDto
     * Converts a Comment entity to a CommentDto.
     * Includes fetching the username.
     *
     * @param comment The Comment entity.
     * @return The corresponding CommentDto.
     */
    private CommentDto convertToDto(Comment comment) {
        if (comment == null) {
            return null;
        }
        CommentDto dto = modelMapper.map(comment, CommentDto.class);
        User user = userMapper.selectById(comment.getUserId());
        if (user != null) {
            dto.setUsername(user.getUsername());
        } else {
            dto.setUsername("Unknown User"); // 处理用户可能已被删除的情况 (Handle case where user might be deleted)
        }
        return dto;
    }
}
