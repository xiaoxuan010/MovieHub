package space.astralbridge.spring.moviehub.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 电影评论实体类
 * Represents a comment entity in the database.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("comments")
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 评论ID (Primary Key)
     * Comment ID (Primary Key)
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID (Foreign Key to users table)
     * User ID (Foreign Key to users table)
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 电影ID (Foreign Key to movies table)
     * Movie ID (Foreign Key to movies table)
     */
    @TableField("movie_id")
    private Long movieId;

    /**
     * 评论内容
     * Comment content
     */
    @TableField("content")
    private String content;

    /**
     * 创建时间 (Auto-filled on insert)
     * Creation timestamp (Auto-filled on insert)
     */
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /**
     * 更新时间 (Auto-filled on insert/update)
     * Update timestamp (Auto-filled on insert/update)
     */
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

}
