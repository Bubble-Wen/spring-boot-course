package top.jwxiang.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class BookCreateDTO {
    @NotBlank(message = "书名不能为空") // 非空校验
    private String title;

    @NotBlank(message = "作者不能为空")
    private String author;

    @NotBlank(message = "ISBN不能为空")
    private String isbn;

    @NotBlank(message = "分类不能为空")
    private String category;

    @NotNull(message = "库存不能为空")
    @PositiveOrZero(message = "库存不能为负数") // 非负校验
    private Integer stock;
}