package top.jwxiang.redis.service.impl;


import com.cloopen.rest.sdk.BodyType;
import com.cloopen.rest.sdk.CCPRestSmsSDK;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.jwxiang.redis.cache.RedisCache;
import top.jwxiang.redis.cache.RedisKeys;
import top.jwxiang.redis.config.CloopenConfig;
import top.jwxiang.redis.enums.ErrorCode;
import top.jwxiang.redis.exception.ServerException;
import top.jwxiang.redis.service.SmsService;
import top.jwxiang.redis.utils.CommonUtils;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class SmsServiceImpl implements SmsService {
    private final CloopenConfig cloopenConfig;
    private final RedisCache redisCache;

    @Override
    public boolean sendSms(String phone) {
        // 校验手机号
        if (!CommonUtils.checkPhone(phone)) {
            throw new ServerException(ErrorCode.PHONE_ERROR);
        }
        // 生成验证码，并且存入Redis，60s有效
        int code = CommonUtils.generateCode();
        redisCache.set(RedisKeys.getSmsKey(phone), code, 60);
        log.info("发送短信验证码：{}", code);
        boolean flag = true;
        flag = send(phone, code);
        return flag;
    }


    public boolean send(String phone, int code) {
        if (!CommonUtils.checkPhone(phone)) {
            throw new ServerException(ErrorCode.PHONE_ERROR);
        }

        code = CommonUtils.generateCode();
        redisCache.set(RedisKeys.getSmsKey(phone), code, 60);
        log.info("发送短信验证码：{}", code);


        //生产环境请求地址
        String serverIp = cloopenConfig.getServerIp();
        //请求端口
        String serverPort = cloopenConfig.getPort();
        //主账号,登陆云通讯网站后,可在控制台首页看到开发者主账号ACCOUNT SID和主账号令牌AUTH TOKEN
        String accountSid = cloopenConfig.getAccountSid();
        String accountToken = cloopenConfig.getAccountToken();
        //请使用管理控制台中已创建应用的APPID
        String appId = cloopenConfig.getAppId();
        String templateId = cloopenConfig.getTemplateId();

        CCPRestSmsSDK sdk = new CCPRestSmsSDK();
        sdk.init(serverIp, serverPort);
        sdk.setAccount(accountSid, accountToken);
        sdk.setAppId(appId);
        sdk.setBodyType(BodyType.Type_JSON);
        String[] datas = {String.valueOf(code), "1"};

        HashMap<String, Object> result = sdk.sendTemplateSMS(phone,templateId,datas,"1234",
                UUID.randomUUID().toString());

        if("000000".equals(result.get("statusCode"))){
            //正常返回输出data包体信息（map）
            HashMap<String,Object> data = (HashMap<String, Object>) result.get("data");
            Set<String> keySet = data.keySet();
            for(String key:keySet){
                Object object = data.get(key);
                log.info("{} = {}", key, object);
            }
        }else{
            // 异常返回输出错误码和错误信息
            log.error("错误码={} 错误信息= {}", result.get("statusCode"), result.get("statusMsg"));
            return false;
        }
        return  true;
    }
}