package top.jwxiang.redis.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.jwxiang.redis.cache.RedisCache;
import top.jwxiang.redis.cache.RedisKeys;
import top.jwxiang.redis.entity.LoginRequest;
import top.jwxiang.redis.entity.LoginResponse;
import top.jwxiang.redis.enums.ErrorCode;
import top.jwxiang.redis.exception.ServerException;
import top.jwxiang.redis.service.LoginService;
import top.jwxiang.redis.utils.CommonUtils;

import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class LoginServiceImpl implements LoginService {
    private final RedisCache redisCache;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        String phone = loginRequest.getPhone();
        String inputCode = loginRequest.getCode();
        // 1.校验手机号格式
        if (!CommonUtils.checkPhone(phone)) {
            throw new ServerException(ErrorCode.PHONE_ERROR);
        }
        // 2.校验验证码是否为空
        if (inputCode == null || inputCode.trim().isEmpty()) {
            throw new ServerException("验证码不能为空");
        }
        // 3.从Redis中获取验证码
        String redisKey = RedisKeys.getSmsKey(phone);
        Object redisCodeObj = redisCache.get(redisKey);
        String redisCode = redisCodeObj != null ? redisCodeObj.toString() : null;
        // 4.验证码不存在或已过期
        if (redisCode == null) {
            throw new ServerException("验证码已过期或不存在");
        }
        // 5.验证码不匹配
        if (!inputCode.equals(redisCode)) {
            throw new ServerException("验证码错误");
        }
        // 6.验证成功，删除Redis中的验证码
        redisCache.delete(redisKey);
        // 7.生成token并返回登录信息
        String token = generateToken(phone);
        log.info("用户 {} 登录成功", phone);
        return new LoginResponse(token, phone);
    }

    private String generateToken(String phone) {
        return UUID.randomUUID().toString().replace("-", "") + phone.hashCode();
    }
}