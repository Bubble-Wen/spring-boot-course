package top.jwxiang.redis.service;

public interface SmsService {
    /**
     * 发送短信
     *
     * @param phone ⼿机号
     */
    boolean sendSms(String phone);
}