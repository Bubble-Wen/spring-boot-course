package top.jwxiang.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.jwxiang.enums.ErrorCode;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    private int code = 0;

    private String msg = "success";

    private T data;

    public static <T> Result<T> ok() {
        return ok(null);
    }

    public static <T> Result<T> ok(T data) {
        Result<T> result = new Result<>();
        result.setData(data);
        return result;
    }

    public static <T> Result<T> error() {
        return error(ErrorCode.SERVER_ERROR);
    }

    public static <T> Result<T> error(String msg) {
        return error(ErrorCode.SERVER_ERROR.getCode(), msg);
    }

    public static <T> Result<T> error(ErrorCode errorCode) {
        return error(errorCode.getCode(), errorCode.getMsg());
    }

    public static <T> Result<T> error(int code, String msg) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(200, "操作成功", data);
    }

    public static Result<Void> success() {
        return new Result<>(200, "操作成功", null);
    }

    public static Result<Void> fail(Integer code, String message) {
        return new Result<>(code, message, null);
    }
}