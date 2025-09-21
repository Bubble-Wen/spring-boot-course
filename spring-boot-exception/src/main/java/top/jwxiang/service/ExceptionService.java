package top.jwxiang.service;

import org.springframework.stereotype.Service;
import top.jwxiang.exception.BusinessException;

@Service
public class ExceptionService {
    /**
     * 模拟未登录异常
     */
    public void unAuthorizedError() {
        throw new BusinessException("权限不足");
    }

    /**
     * 模拟系统异常
     */
    public void systemError() {
        throw new BusinessException("系统异常");
    }
    }
