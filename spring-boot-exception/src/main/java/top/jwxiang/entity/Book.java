package top.jwxiang.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("book")
public class Book {
    @TableId(type = IdType.AUTO)
    private Long id;                 // 对应表中id

    private String title;            // 对应表中title
    private String author;           // 对应表中author
    private String isbn;             // 对应表中isbn
    private String category;         // 对应表中category
    private Integer stock;           // 对应表中stock

    @TableLogic                      // 逻辑删除注解
    private Integer deleted;         // 对应表中deleted

    @Version                         // 乐观锁注解
    private Integer version;         // 对应表中version

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;// 对应表中create_time

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;// 对应表中update_time
}