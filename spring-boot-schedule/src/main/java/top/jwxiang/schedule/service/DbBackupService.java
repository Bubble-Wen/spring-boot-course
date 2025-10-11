package top.jwxiang.schedule.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import top.jwxiang.schedule.config.DbConfig;

import java.io.File;
import java.io.IOException;

@Slf4j
@Service
@AllArgsConstructor
public class DbBackupService {
    private final DbConfig dbConfig;

    private static final String MYSQL_DUMP_PATH = "C:\\Program Files\\MySQL\\MySQL Server 8.0\\bin\\mysqldump.exe";


    /**
     * 每日凌晨2:00执行备份（测试用每15秒执行）
     */
    @Scheduled(cron = "*/15 * * * * ?")
    public void backupWithHutool() {
        try {
            // 2. 读取配置+构建参数（包含端口）
            String host = dbConfig.getHost();
            int port = Integer.parseInt(dbConfig.getPort());
            // 解决端口类型问题
            String user = dbConfig.getUsername();
            String password = dbConfig.getPassword();
            String database = dbConfig.getDbName();
            String localPath = dbConfig.getLocalPath();
            String backupFile = localPath + database + "_" + System.currentTimeMillis() + ".sql";

            // 3. 自动创建备份目录（避免文件写入失败）
            File backupDir = new File(localPath);
            if (!backupDir.exists()) {
                boolean isCreated = backupDir.mkdirs();
                log.info("备份目录创建结果：{}，路径：{}", isCreated ? "成功" : "失败", localPath);
                if (!isCreated) {
                    log.error("备份目录创建失败，终止备份");
                    return;
                }
            }

            // 4. 构建 mysqldump 命令（指定完整路径+显式传端口）
            CommandLine command = new CommandLine(MYSQL_DUMP_PATH);
            // 用完整路径跳过环境变量
            command.addArgument("-h" + host, false);
            // 主机
            command.addArgument("-P" + port, false);
            // 端口（关键：之前未传）
            command.addArgument("-u" + user, false);
            // 用户名
            command.addArgument("-p" + password, false);
            // 密码（生产环境用配置文件）
            command.addArgument(database, false);
            // 要备份的数据库
            command.addArgument("--result-file=" + backupFile, false);
            // 备份文件路径

            // 5. 执行命令
            DefaultExecutor executor = new DefaultExecutor();
            PumpStreamHandler streamHandler = new PumpStreamHandler(System.out, System.err);
            executor.setStreamHandler(streamHandler);
            executor.execute(command);

            log.info("✅ 数据库备份成功！文件路径：{}", backupFile);
        } catch (NumberFormatException e) {
            log.error("❌ 端口格式错误（需为数字）", e);
        } catch (IOException e) {
            log.error("❌ 数据库备份失败（检查mysqldump路径、数据库连接）", e);
        }
    }
}
