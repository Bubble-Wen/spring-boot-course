package top.jwxiang.schedule.service;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
public class DailyWeatherService {

    @Resource
    private JavaMailSender mailSender;

    // 从配置文件注入邮件参数（仅保留邮件相关配置）
    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${email.recipients}")
    private String[] toEmails;

    /**
     * 定时任务：每60秒执行一次（无外部API依赖）
     */
    @Scheduled(cron = "0 */1 * * * ?")
    public void sendDailyWeather() {
        try {
            log.info("【天气邮件】开始执行（本地模拟数据）...");

            // 1. 本地模拟天气数据（无需调用外部API）
            String cityName = "南京市";
            String currentTime = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm:ss"));
            // 模拟实时天气（可根据需要调整）
            String weather = "晴";
            String temp = "25";
            // 温度
            String windDir = "东北风";
            String windScale = "2级";
            String humidity = "50";
            // 湿度

            // 2. 构造邮件内容
            String subject = String.format("【实时天气快报】%s %s天气", currentTime, cityName);
            String content = String.format("""
                    🕒 发送时间：%s
                    🏙️ 城市：%s
                    🌤️ 天气：%s
                    🌡️ 温度：%s℃
                    💨 风向：%s
                    🌬️ 风力：%s
                    💧 湿度：%s%%
                    💡 提示：出门请根据天气增减衣物，注意交通安全！
                    📌 数据来源：本地模拟（无外部API依赖）""",
                    currentTime, cityName, weather, temp, windDir, windScale, humidity);

            // 3. 发送邮件
            sendSimpleEmail(subject, content);
            log.info("【天气邮件】发送成功！收件人：{}", String.join(",", toEmails));

        } catch (Exception e) {
            log.error("【天气邮件】执行失败", e);
        }
    }


    /**
     * 发送简单文本邮件
     */
    private void sendSimpleEmail(String subject, String content) {
        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setFrom(fromEmail);
            // 发件人（与配置文件一致）
            mail.setTo(toEmails);
            // 收件人
            mail.setSubject(subject);
            // 邮件主题
            mail.setText(content);
            // 邮件内容
            mailSender.send(mail);
            // 发送邮件
        } catch (Exception e) {
            log.error("【邮件发送】失败", e);
            throw new RuntimeException("邮件发送失败：" + e.getMessage(), e);
        }
    }
}