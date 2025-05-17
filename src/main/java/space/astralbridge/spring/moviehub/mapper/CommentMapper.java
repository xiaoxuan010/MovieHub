package space.astralbridge.spring.moviehub.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import space.astralbridge.spring.moviehub.entity.Comment;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

}