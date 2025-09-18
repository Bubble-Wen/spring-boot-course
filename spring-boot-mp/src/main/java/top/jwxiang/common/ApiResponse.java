package top.jwxiang.common;

import lombok.Getter;

@Getter
public class ApiResponse<T> {
    private final int code;
    private final String msg;
    private final T data;
    // 私有构造器，只允许内部调⽤
    private ApiResponse(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
    // 成功响应（带数据）
    public static <T> ApiResponse<T> success(String msg, T data) {
        return new ApiResponse<>(200, msg, data);
    }
    // 成功响应（不带数据）
    public static <T> ApiResponse<T> success(String msg) {
        return new ApiResponse<>(200, msg, null);
    }
    // 错误响应
    public static <T> ApiResponse<T> error(String msg) {
        return new ApiResponse<>(400, msg, null);
    }
}