package top.jwxiang.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统一接口响应封装
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    // 状态码：200成功，400参数错误，404资源不存在，500系统错误
    private Integer code;
    // 提示信息
    private String message;
    // 响应数据（成功时返回，失败时为null）
    private T data;
    // 时间戳（毫秒级，便于前端定位请求时间）
    private Long timestamp;

    // 构造方法：自动填充时间戳
    public ApiResponse(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    // 成功（带数据）
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, "操作成功", data);
    }

    // 成功（无数据）
    public static ApiResponse<Void> success() {
        return new ApiResponse<>(200, "操作成功", null);
    }

    // 失败（自定义状态码和信息）
    public static ApiResponse<Void> fail(Integer code, String message) {
        return new ApiResponse<>(code, message, null);
    }
}