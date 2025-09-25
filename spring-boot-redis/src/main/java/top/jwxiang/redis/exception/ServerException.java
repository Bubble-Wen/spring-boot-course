package top.jwxiang.redis.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.jwxiang.redis.enums.ErrorCode;
@Data
@EqualsAndHashCode(callSuper = true)
public class ServerException extends RuntimeException {

    private int code;
    private String msg;

    public ServerException(String message) {
        super(message);
        this.code = ErrorCode.INTERNAL_SERVER_ERROR.getCode();
        this.msg = message;
    }

    public ServerException(ErrorCode errorCode) {
        super(errorCode.getMsg());
        this.code = errorCode.getCode();
        this.msg = errorCode.getMsg();
    }

    public ServerException(String msg, Throwable e) {
        super(msg, e);
        this.code = ErrorCode.INTERNAL_SERVER_ERROR.getCode();
        this.msg = msg;
    }
}
