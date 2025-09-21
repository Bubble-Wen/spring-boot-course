package top.jwxiang.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StockAdjustDTO {
    @NotNull(message = "调整数量不能为空")
    @Min(value = 1, message = "调整数量不能小于1")
    private Integer amount;
    private String type; // 类型：in（入库）/ out（出库）
}
