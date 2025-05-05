package space.astralbridge.spring.moviehub.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import space.astralbridge.spring.moviehub.entity.Comment;

/**
 * 评论 Mapper 接口
 * MyBatis Mapper interface for Comment entity.
 *
 * @author Gemini
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
}
