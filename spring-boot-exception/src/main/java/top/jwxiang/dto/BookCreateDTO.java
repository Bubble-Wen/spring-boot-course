package top.jwxiang.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookCreateDTO {
    @NotBlank(message = "书名不能为空")
    private String title;
    private String author;
    @NotBlank(message = "ISBN不能为空")
    private String isbn;
    private String category;
    @NotNull(message = "库存不能为空")
    private Integer stock;
}