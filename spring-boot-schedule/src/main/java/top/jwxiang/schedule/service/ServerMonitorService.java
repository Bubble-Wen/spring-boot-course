package top.jwxiang.schedule.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.platform.windows.WindowsHardwareAbstractionLayer;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
public class ServerMonitorService {

    private final HardwareAbstractionLayer hardware;
    private final DecimalFormat df = new DecimalFormat("#.00");

    @Value("${server.monitor.cpu-threshold}")
    private double cpuThreshold;

    @Value("${server.monitor.memory-threshold}")
    private double memoryThreshold;

    /**
     * 初始化服务器硬件信息（Windows系统专用）
     */
    public ServerMonitorService() {
        // 关键修正：使用Windows系统的硬件抽象层
        this.hardware = new WindowsHardwareAbstractionLayer();
    }

    /**
     * 服务器监控任务，每10秒执行一次
     */
    @Scheduled(cron = "*/10 * * * * ?")
    public void monitorServerHealth() {
        try {
            double cpuUsage = getCpuUsage();
            double memoryUsage = getMemoryUsage();
            String monitorLog = String.format(
                    "【服务器监控】时间：%s，CPU使用率：%s%%，内存使用率：%s%%，阈值：CPU<%s%%、内存<%s%%",
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                    df.format(cpuUsage),
                    df.format(memoryUsage),
                    cpuThreshold,
                    memoryThreshold
            );
            log.info(monitorLog);

            if (cpuUsage > cpuThreshold) {
                log.warn("⚠️ 【告警】CPU使用率超过阈值！当前：{}%，阈值：{}%", df.format(cpuUsage), cpuThreshold);
            }
            if (memoryUsage > memoryThreshold) {
                log.warn("⚠️ 【告警】内存使用率超过阈值！当前：{}%，阈值：{}%", df.format(memoryUsage), memoryThreshold);
            }
        } catch (Exception e) {
            log.error("【服务器监控】执行失败", e);
        }
    }

    private double getCpuUsage() {
        CentralProcessor processor = hardware.getProcessor();
        double systemCpuLoad = processor.getSystemCpuLoad(1000);
        // 1秒内的CPU负载采样
        return systemCpuLoad * 100;
    }

    private double getMemoryUsage() {
        GlobalMemory memory = hardware.getMemory();
        long totalMemory = memory.getTotal();
        long usedMemory = totalMemory - memory.getAvailable();
        return (double) usedMemory / totalMemory * 100;
    }
}