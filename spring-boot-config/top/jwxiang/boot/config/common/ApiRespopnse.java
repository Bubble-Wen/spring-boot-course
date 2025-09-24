package top.jwxiang.boot.config.common;

import lombok.Getter;

@Getter
public class ApiRespopnse<T> {
    private final int code;
    private final String msg;
    private final  T data;

    public ApiRespopnse(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> ApiRespopnse<T> success(String msg, T data) {
        return new ApiRespopnse<>(200, msg, data);
    }

    public static <T> ApiRespopnse<T> error(String msg) {
        return new ApiRespopnse<>(400, msg, null);
    }
}
