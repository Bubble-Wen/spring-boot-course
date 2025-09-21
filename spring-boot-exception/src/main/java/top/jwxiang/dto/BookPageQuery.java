package top.jwxiang.dto;

import lombok.Data;

@Data
public class BookPageQuery {
    private Integer pageNum = 1;   // 页码，默认第1页
    private Integer pageSize = 10; // 页大小，默认10条
    private String title;          // 书名模糊查询
    private String category;       // 分类精确查询
}