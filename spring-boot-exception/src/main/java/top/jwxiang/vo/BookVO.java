package top.jwxiang.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookVO {
    private Long id; // 图书ID
    private String title; // 书名
    private String author; // 作者
    private String isbn; // ISBN
    private String category; // 分类
    private Integer stock; // 库存
    private LocalDateTime createTime; // 创建时间
    private LocalDateTime updateTime; // 更新时间
    // 隐藏deleted、version等后端字段，不返回给前端
}