package top.jwxiang.redis.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 错误码枚举类
 * @author jwxiang
 * @date 2021/1/23
 */
@AllArgsConstructor
@Getter
public enum ErrorCode {
    PARAM_ERROR("参数错误", 400),
    UNAUTHORIZED("登录失败，请重新登录", 401),
    NOT_FOUND("用户不存在", 404),
    NOT_PERMISSION("权限不足", 403),
    METHOD_ERROR("方法不允许", 405),
    SERVER_ERROR("服务器异常，请稍后再试", 500),

    PHONE_ERROR("手机号格式错误", 400),
    INTERNAL_SERVER_ERROR("服务器内部错误", 500);

        private final String msg;
        private final int code;

}
