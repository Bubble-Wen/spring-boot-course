package top.jwxiang.schedule.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
@Slf4j
public class ScheduledConfig {

    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        // 1. 设置核心线程数（根据任务数量调整，建议 5-20）
        scheduler.setPoolSize(10);
        // 2. 设置线程名称前缀（便于日志排查）
        scheduler.setThreadNamePrefix("scheduled-task-");
        // 3. 设置任务拒绝策略（任务太多时如何处理，默认为 AbortPolicy 抛出异常）
        scheduler.setRejectedExecutionHandler((runnable, executor) -> {
            log.warn("定时任务队列已满，任务被拒绝：{}", runnable.getClass().getSimpleName());
        });
        // 4. 设置任务执行完成后的线程存活时间（60秒）
        scheduler.setAwaitTerminationSeconds(60);
        // 5. 初始化调度器
        scheduler.initialize();
        return scheduler;
    }
}