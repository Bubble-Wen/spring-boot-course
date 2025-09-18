package top.jwxiang.utils;

import jakarta.annotation.Resource;
import net.datafaker.Faker;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import top.jwxiang.entity.UserAccount;
import top.jwxiang.service.UserAccountService;

import java.util.List;
import java.util.Locale;
import java.util.stream.IntStream;

@Component
public class DataFakerUtil {
    private static final Faker ZH_FAKER = new Faker(Locale.CHINA);
    private static final Faker EN_FAKER = new Faker(Locale.ENGLISH);
    private static final String RAW_PASSWORD = "123456";
    private static final String ENCODED_PASSWORD = new BCryptPasswordEncoder().encode(RAW_PASSWORD);
    @Resource
    private UserAccountService userAccountService;
    private UserAccount generateOne(int i) {
        UserAccount user = new UserAccount();
        // ⽣成多了会重复的，可以⾃⼰再添加唯⼀标识，⽐如 UUID.randomUUID().toString().substring(0, 8)
        String username = EN_FAKER.internet().username();
        user.setUsername(username);
        user.setPassword(ENCODED_PASSWORD);
        user.setNickname(ZH_FAKER.name().fullName());
        user.setEmail(username + "@example.com");
        user.setPhone(ZH_FAKER.phoneNumber().cellPhone());
        user.setAvatarUrl(ZH_FAKER.avatar().image());
        user.setStatus(1);
        user.setDeleted(0);
        user.setVersion(0);
        return user;
    }
    public void generateBatch() {
        // 1000 条数据，每次 100 条
        int total = 1000;
        int step = 100;
        for (int start = 0; start < total; start += step) {
            List<UserAccount> batch = IntStream.range(start, start + step).mapToObj(this::generateOne).toList();
            userAccountService.saveBatch(batch, 1000);
        }
    }
}