package top.jwxiang.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StockAdjustDTO {
    @NotNull(message = "调整数量不能为空")
    @Min(value = 1, message = "调整数量不能小于1") // 至少调整1本
    private Integer amount;

    @NotBlank(message = "调整类型不能为空")
    private String type; // in=入库，out=出库
}