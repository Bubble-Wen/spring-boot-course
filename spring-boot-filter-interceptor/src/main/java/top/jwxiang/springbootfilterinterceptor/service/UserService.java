package top.jwxiang.springbootfilterinterceptor.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.jwxiang.springbootfilterinterceptor.dto.LoginRequest;
import top.jwxiang.springbootfilterinterceptor.entity.User;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * 用户服务类（模拟数据库操作）
 */
@Service
@Slf4j
public class UserService {

    // 定义一个集合，用来存放用户数据,SET 可以去重
    private static final Set<User> MOCK_USERS = new HashSet<>();

    /*
     * 静态代码块，初始化用户数据，它会比构造方法先执行
     */
    static {
        MOCK_USERS.add(new User(1L, "admin", "123456", "admin@example.com", "13800000001", "ADMIN"));
        MOCK_USERS.add(new User(2L, "user", "123456", "user@example.com", "13800000002", "USER"));
    }

    /**
     * 用户登录
     */
    public User authenticate(LoginRequest loginRequest) {
        log.info("用户登录验证：{},{}", loginRequest.getUsername(), loginRequest.getPassword());
        // 使用 Java 8 的Stream API ,在集合 MOCK_USERS 中查找匹配的用户
        // 通过过滤条件用户名和密码都相等来搜索，找到第一个匹配项就返回该用户对象，如果没找到则返回 null，这是典型的用户登录验证逻辑。
        return MOCK_USERS.stream()
                .filter(u -> {
                    // 检查用户名是否匹配
                    if (!loginRequest.getUsername().equals(u.getUsername())) {
                        return false;
                    }
                    // 安全地比较密码，处理可能为 null 的情况
                    String userPassword = u.getPassword();
                    String requestPassword = loginRequest.getPassword();
                    return Objects.equals(userPassword, requestPassword);
                })
                .findFirst()
                .orElse(null);
    }

    /**
     * 根据用户名获取用户信息
     */
    public User getUserByUsername(String username) {
        return MOCK_USERS.stream().filter(user -> Objects.equals(user.getUsername(), username)).findFirst().orElse(null);
    }

}