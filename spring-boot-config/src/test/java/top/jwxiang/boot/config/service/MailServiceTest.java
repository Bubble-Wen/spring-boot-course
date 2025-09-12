package top.jwxiang.boot.config.service;


import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.jwxiang.boot.config.model.Mail;

@SpringBootTest
class MailServiceTest {
    @Resource
    private MailService mailService;

    @Test
    void sendSimpleMail() {
        Mail mail = new Mail();
        mail.setTo("2933128731@qq.com");
        mail.setSubject("测试简单邮件");
        mail.setContent("简单邮件内容");
        mailService.sendSimpleMail(mail);
    }

    @Test
    void sendHTMLMail() {
        Mail mail = new Mail();
        mail.setTo("2933128731@qq.com");
        mail.setSubject("测试HTML邮件");
        mail.setContent("<html><body><h1>测试HTML邮件</h1></body></html>");
        mailService.sendHtmlMail(mail);
    }

}