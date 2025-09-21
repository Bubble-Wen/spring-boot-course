package top.jwxiang.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BookUpdateDTO {
    @NotBlank(message = "书名不能为空")
    private String title;
    private String author;
    private String category;
}