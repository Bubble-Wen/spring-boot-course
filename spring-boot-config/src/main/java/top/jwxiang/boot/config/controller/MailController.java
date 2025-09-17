package top.jwxiang.boot.config.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.jwxiang.boot.config.common.ApiRespopnse;
import top.jwxiang.boot.config.enums.ResultStatus;
import top.jwxiang.boot.config.model.Mail;
import top.jwxiang.boot.config.service.MailService;

@RestController
@RequestMapping("/mail")
public class MailController {
    @Autowired
    private MailService mailService;

    @PostMapping("/simple")
    public ResponseEntity<ApiRespopnse<ResultStatus>> sendSimpleMail(@Valid @RequestBody Mail mail) {
        ResultStatus rs=mailService.sendSimpleMail(mail);
        if (rs==ResultStatus.SUCCESS){
            return ResponseEntity.ok(ApiRespopnse.success("发送成功",rs));
        }
        return ResponseEntity.badRequest().body(ApiRespopnse.error("发送失败"));
    }

    @PostMapping("/html")
    public ResponseEntity<ApiRespopnse<ResultStatus>> sendHtmlMail(@Valid @RequestBody Mail mail) {
        ResultStatus rs=mailService.sendHtmlMail(mail);
        return rs==ResultStatus.SUCCESS?
                ResponseEntity.ok(ApiRespopnse.success("发送成功",rs)):
                ResponseEntity.badRequest().body(ApiRespopnse.error("发送失败"));
    }
}
