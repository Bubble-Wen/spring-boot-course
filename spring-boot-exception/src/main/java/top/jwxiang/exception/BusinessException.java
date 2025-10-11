package top.jwxiang.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.jwxiang.enums.ErrorCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BusinessException extends RuntimeException {
    private int code;
    private String msg;

    public BusinessException(String message) {
        this.code = ErrorCode.SERVER_ERROR.getCode();
        this.msg = message;
    }

    public BusinessException(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.msg = errorCode.getMsg();
    }

    public BusinessException(String message,Throwable e) {
         this.code = ErrorCode.SERVER_ERROR.getCode();
         this.msg = message;
    }
}
