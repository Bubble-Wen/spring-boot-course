//package top.jwxiang.week1.config
//
//import jakarta.annotation.Resource;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//import top.sxmeng.week1.service.SmsService;
//
//@RestController
//public class SmsController {
//    @Resource
//    private SmsService smsService;
//
//    @GetMapping("/sms")
//    public void sms(String phone){
//        smsService.sendSms(phone);
//    }
//}
