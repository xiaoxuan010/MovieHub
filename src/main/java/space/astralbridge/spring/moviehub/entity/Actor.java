package space.astralbridge.spring.moviehub.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import jakarta.annotation.Nullable;
import lombok.Data;

@Data
@TableName("actor")
public class Actor {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    @Nullable
    private String photo;
    private String description = "";
}
