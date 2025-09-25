package top.jwxiang.redis.handler;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import top.jwxiang.redis.exception.ServerException;
import top.jwxiang.redis.result.Result;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 处理自定义业务异常
    @ExceptionHandler(ServerException.class)
    public Result<String> handleServerException(ServerException e) {
        log.warn("自定义业务异常: code={}, msg={}", e.getCode(), e.getMsg());
        return Result.error(e.getCode(), e.getMsg());
    }

    // 处理 @Valid + @RequestBody 形式的参数校验异常（JSON 请求体）
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<String> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        FieldError fieldError = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .orElse(null);
        String errorMsg = fieldError != null ?
                fieldError.getField() + " " + fieldError.getDefaultMessage() : "请求参数不合法";
        return Result.error(400, errorMsg);
    }

    // 处理表单/路径参数绑定校验异常（非 @RequestBody 场景）
    @ExceptionHandler(BindException.class)
    public Result<String> handleBindException(BindException ex) {
        FieldError fieldError = ex.getFieldError();
        String errorMsg = fieldError != null ?
                fieldError.getField() + " " + fieldError.getDefaultMessage() : "请求参数不合法";
        return Result.error(400, errorMsg);
    }

    // 处理单个参数（@RequestParam/@PathVariable）的校验异常（如 @Min 等约束）
    @ExceptionHandler(ConstraintViolationException.class)
    public Result<String> handleConstraintViolation(ConstraintViolationException ex) {
        String errorMsg = ex.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + " " + violation.getMessage())
                .collect(Collectors.joining("; "));
        return Result.error(400, errorMsg);
    }

    // 处理请求体反序列化、类型不匹配、缺少请求参数等常见 400 错误
    @ExceptionHandler({
            HttpMessageNotReadableException.class,
            MethodArgumentTypeMismatchException.class,
            MissingServletRequestParameterException.class
    })
    public Result<String> handleBadRequest(Exception ex) {
        return Result.error(400, "请求格式错误");
    }

    // 兜底处理未知异常
    @ExceptionHandler(Exception.class)
    public Result<String> handleUnknownException(Exception ex) {
        log.error("未知异常发生", ex);
        return Result.error(500, "服务器异常，请稍后再试");
    }
}