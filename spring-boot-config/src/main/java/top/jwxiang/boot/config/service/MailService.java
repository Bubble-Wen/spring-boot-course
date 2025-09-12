package top.jwxiang.boot.config.service;

import top.jwxiang.boot.config.enums.ResultStatus;
import top.jwxiang.boot.config.model.Mail;

public interface MailService {
    ResultStatus sendSimpleMail(Mail mail);
    ResultStatus sendHtmlMail(Mail mail);
}
