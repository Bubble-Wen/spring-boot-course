package top.jwxiang;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("top.jwxiang.mapper") // 替换为你的Mapper接口实际包路径
public class ExceptionApplication {
    public static void main(String[] args) {
        SpringApplication.run(ExceptionApplication.class, args);
    }
}