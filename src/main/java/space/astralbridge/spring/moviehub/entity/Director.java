package space.astralbridge.spring.moviehub.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("director")
public class Director {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String photo;
    private String description;
}
