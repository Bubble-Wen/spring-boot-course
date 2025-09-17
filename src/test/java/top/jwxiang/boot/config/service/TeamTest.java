package top.jwxiang.boot.config.service;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.jwxiang.boot.config.model.Team;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Slf4j
class TeamTest {
    @Resource
    private Team team;

    @Test
    void testTeam() {
        log.info("团队创建成功");
        assertEquals("Web2班", team.getName());
        assertEquals("mqxu", team.getLeader());

    }
}