package top.jwxiang.service.impl;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import top.jwxiang.cache.RedisCache;
import top.jwxiang.cache.RedisKeys;
import top.jwxiang.enums.ErrorCode;
import top.jwxiang.exception.ServerException;
import top.jwxiang.service.SmsService;
import top.jwxiang.utils.CommonUtils;


@Service
public class SmsServiceImpl implements SmsService {

    @Resource
    private RedisCache redisCache;

    @Override
    public void sendSms(String phone) {
        // 校验手机号格式
        if (!CommonUtils.checkPhone(phone)) {
            throw new ServerException(ErrorCode.PHONE_ERROR);
        }
        // 生成验证码
        int code = CommonUtils.generateCode();
        // 存入Redis（5分钟过期）
        redisCache.set(RedisKeys.getSmsKey(phone), code, 300);
        // 此处可添加真实发送短信的逻辑（调用第三方API）
        System.out.println("向手机号 " + phone + " 发送验证码：" + code);
    }
}