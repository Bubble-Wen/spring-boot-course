package top.jwxiang.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.jwxiang.common.ApiResponse;

import java.util.stream.Collectors;

/**
 * 全局异常处理器：所有@RestController接口异常统一处理
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 1. 参数校验异常（@Valid触发，如书名非空、ISBN格式错误）
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleValidException(MethodArgumentNotValidException e) {
        String errorMsg = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("；"));
        return ApiResponse.fail(400, errorMsg);
    }

    /**
     * 2. 资源不存在异常（如图书ID不存在）
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse<Void> handleResourceNotFound(ResourceNotFoundException e) {
        return ApiResponse.fail(404, e.getMessage());
    }

    /**
     * 3. 库存不足异常（借书/出库场景）
     */
    @ExceptionHandler(InsufficientStockException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleInsufficientStock(InsufficientStockException e) {
        return ApiResponse.fail(400, e.getMessage());
    }

    /**
     * 4. ISBN重复异常（新增图书时ISBN已存在）
     */
    @ExceptionHandler(org.springframework.dao.DuplicateKeyException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiResponse<Void> handleDuplicateIsbn(org.springframework.dao.DuplicateKeyException e) {
        return ApiResponse.fail(409, "ISBN已存在，无法重复添加");
    }

    /**
     * 5. 自定义ISBN重复异常
     */
    @ExceptionHandler(DuplicateIsbnException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiResponse<Void> handleDuplicateIsbn(DuplicateIsbnException e) {
        return ApiResponse.fail(409, e.getMessage());
    }

    /**
     * 6. 库存调整类型错误异常
     */
    @ExceptionHandler(InvalidStockAdjustTypeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleInvalidStockAdjustType(InvalidStockAdjustTypeException e) {
        return ApiResponse.fail(400, e.getMessage());
    }

    /**
     * 7. 系统未知异常（兜底处理）
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<Void> handleUnknownException(Exception e) {
        e.printStackTrace(); // 生产环境替换为日志框架（如Logback）
        return ApiResponse.fail(500, "系统异常，请联系管理员");
    }
}