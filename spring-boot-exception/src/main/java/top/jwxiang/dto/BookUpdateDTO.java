package top.jwxiang.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data // 自动生成getter、setter
public class BookUpdateDTO {
    @NotBlank(message = "书名不能为空") // 校验：书名必填
    private String title;

    @NotBlank(message = "作者不能为空") // 校验：作者必填
    private String author;

    @NotBlank(message = "分类不能为空") // 校验：分类必填
    private String category;

    @PositiveOrZero(message = "库存不能为负数") // 校验：库存>=0
    private Integer stock;

    private Integer version; // 乐观锁版本号（必须传递，否则更新失败）
}