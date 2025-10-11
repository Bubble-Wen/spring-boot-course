package top.jwxiang.redis.entity;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

@SpringBootTest
class StudentTest {
@Resource
    private RedisTemplate<String, Object> redisTemplate;

@Test
    void testStudent() {
        Address address = Address.builder()
                .province("北京")
                .city("北京")
                .build();
        Student student = Student.builder()
                .name("jwx")
                .address(address)
                .build();
        redisTemplate.opsForValue().set("student", student,30, TimeUnit.SECONDS);
    }
}