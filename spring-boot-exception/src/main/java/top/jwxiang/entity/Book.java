package top.jwxiang.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("book")
public class Book {
    @TableId(type = IdType.AUTO)
    private Long id;            // 自增主键
    private String title;       // 书名
    private String author;      // 作者
    private String isbn;        // ISBN（唯一）
    private String category;    // 分类
    private Integer stock;      // 库存
    @TableLogic                // 逻辑删除标记（0-未删，1-已删）
    private Integer deleted;
    @Version                   // 乐观锁版本号
    private Integer version;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime; // 创建时间（自动填充）
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime; // 更新时间（自动填充）
}