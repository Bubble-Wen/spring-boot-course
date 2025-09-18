package top.jwxiang.boot.config.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import top.jwxiang.boot.config.model.Team;

@RestController
@Slf4j
public class TeamController {
    @PostMapping("/team")
    public ResponseEntity<Team> createTeam(@Valid @RequestBody Team team){
        return ResponseEntity.ok(team);
    }
}