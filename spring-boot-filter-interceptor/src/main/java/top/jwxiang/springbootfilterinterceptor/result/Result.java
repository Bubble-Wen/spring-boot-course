package top.jwxiang.springbootfilterinterceptor.result;

import lombok.Data;

/**
 * 统一返回结果封装类
 *
 * @author dwy
 */
@Data
public class Result<T> {
    private int code;
    // 状态码（200成功，非200失败）
    private String message;
    // 响应信息
    private T data;
    // 响应数据

    // 私有构造：禁止直接实例化
    private Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // 成功响应（带数据）
    public static <T> Result<T> ok(T data) {
        return new Result<>(200, "success", data);
    }

    // 成功响应（无数据）
    public static <T> Result<T> ok() {
        return new Result<>(200, "success", null);
    }

    // 失败响应
    public static <T> Result<T> error(int code, String message) {
        return new Result<>(code, message, null);
    }

    // 简化失败响应（默认400状态码）
    public static <T> Result<T> error(String message) {
        return new Result<>(400, message, null);
    }
}