package top.jwxiang.schedule.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class TaskService {

    // 1. 固定延迟执行：上一次任务结束后，延迟 fixedDelay 毫秒执行下一次
    // 场景：任务执行时间不固定，需等上一次完成（如数据同步，避免重复处理）
    @Scheduled(fixedDelay = 5000) // 延迟5秒后开始执行
    public void fixedDelayTask() {
        log.info("【固定延迟任务】执行时间：{}，执行线程：{}", LocalDateTime.now(), Thread.currentThread().getName());
        // 模拟任务执行耗时（1秒）
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // 2. 固定频率执行：上一次任务开始后，间隔 fixedRate 毫秒执行下一次
    // 场景：任务执行时间短，需按固定频率执行（如实时监控）
    @Scheduled(fixedRate = 5000) // 频率为每5秒执行一次
    public void fixedRateTask() {
        log.info("【固定频率任务】执行时间：{}，执行线程：{}", LocalDateTime.now(), Thread.currentThread().getName());
    }

    // 3. 初始延迟执行：项目启动后，延迟 initialDelay 毫秒，再按 fixedRate 执行
    // 场景：任务依赖其他组件初始化（如数据库连接池启动）
    @Scheduled(initialDelay = 10000, fixedRate = 5000) // 启动后10秒开始，每5秒执行
    public void initialDelayTask() {
        log.info("【初始延迟任务】执行时间：{},执行线程：{}", LocalDateTime.now(), Thread.currentThread().getName());
    }

    // 4. Cron 表达式执行：支持复杂时间规则（最灵活）
    // 场景：固定时间执行，如 cron="0 0 0 * * ?"（每天0点） cron="0 0 14 ? * MON"（星期一的14点）
    @Scheduled(cron = "0 * * * * ?") // 每分钟执行一次
    public void cronTask() {
        log.info("【Cron任务】执行时间：{},执行线程：{}", LocalDateTime.now(), Thread.currentThread().getName());
    }
}
