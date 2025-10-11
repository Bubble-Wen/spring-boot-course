package top.jwxiang.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.jwxiang.common.Result;
import top.jwxiang.exception.BusinessException;

@RestControllerAdvice
@Slf4j
public class BusinessExceptionHandler {

    /**
     * 处理业务异常
     *
     * @parame
     * @return
     */
    @ExceptionHandler(BusinessException.class)
    public Result<String> handleBusinessException(BusinessException e) {
        return Result.error(e.getCode(), e.getMsg());
    }

    /**
     * 处理其他异常
     *
     * @parame
     */
    @ExceptionHandler(Exception.class)
    public void handleException(Exception e) {
        log.error(e.getMessage(), e);
    }
}
