package top.jwxiang.quickstart.service.impl;

import com.cloopen.rest.sdk.BodyType;
import com.cloopen.rest.sdk.CCPRestSmsSDK;
import jakarta.annotation.Resource;
import top.jwxiang.quickstart.config.CloopenConfig;
import top.jwxiang.quickstart.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service
public class SmsServiceImpl implements SmsService {
    @Resource
    private CloopenConfig cloopenConfig;

    @Override
    public void sendSms(String phone) {
        int code = ThreadLocalRandom.current().nextInt(1000, 9999);
        log.info("发送短信验证码：{}", code);

        //生产环境请求地址
        String serverIp = cloopenConfig.getServerIp();
        //请求端口
        String serverPort = cloopenConfig.getPort();
        //主账号,登陆云通讯网站后,可在控制台首页看到开发者主账号ACCOUNT SID和主账号令牌AUTH TOKEN
        String accountSId = cloopenConfig.getAccountSId();
        String accountToken = cloopenConfig.getAccountToken();
        //请使用管理控制台中已创建应用的APPID
        String appId = cloopenConfig.getAppId();
        String templateId = cloopenConfig.getTemplateId();

        CCPRestSmsSDK sdk = new CCPRestSmsSDK();
        sdk.init(serverIp, serverPort);
        sdk.setAccount(accountSId, accountToken);
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
            System.out.println("错误码=" + result.get("statusCode") +" 错误信息= "+result.get("statusMsg"));
        }
    }
}
