package top.jwxiang.quickstart.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    // 读取开关状态
    @Value("${my.feature.helloSwitch}")
    private boolean isHelloEnabled;

    @Value("${my.feature.closeMsg}")
    private String closeMsg;

    @GetMapping("/hello")
    public String hello() {
        if (isHelloEnabled) {
            return "接口开放中！";
        }else {
            return closeMsg;
        }
    }
}
