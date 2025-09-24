package top.jwxiang.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.jwxiang.result.Result;
import top.jwxiang.service.SmsService;

@RestController
public class SmsController {
    @Resource
    private SmsService smsService;

    @GetMapping("/sms")
    public Result<Boolean> sendSms(@RequestParam String phone) {
        smsService.sendSms(phone);
        return Result.ok(true);
    }
}
