package top.jwxiang.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.jwxiang.common.Result;

@RestControllerAdvice
@Slf4j
public class GlobalException {

    // 处理图书不存在异常（返回404）
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Result<Void>> handleResourceNotFound(ResourceNotFoundException e) {
        log.error("资源不存在：", e);
        Result<Void> result = Result.fail(404, e.getMessage());
        return new ResponseEntity<>(result, HttpStatus.NOT_FOUND); // HTTP状态码404
    }

    // 处理业务逻辑异常（如版本号过期，返回400）
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Result<Void>> handleBusinessException(RuntimeException e) {
        log.error("业务处理失败：", e);
        Result<Void> result = Result.fail(400, e.getMessage());
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST); // HTTP状态码400
    }

    // 处理其他未捕获的异常（返回500）
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result<Void>> handleAllException(Exception e) {
        log.error("服务器内部错误：", e);
        Result<Void> result = Result.fail(500, "服务器内部错误：" + e.getMessage());
        return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR); // HTTP状态码500
    }
}
